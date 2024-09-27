package com.xiaofan.user.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaofan.apicommon.common.ErrorCode;
import com.xiaofan.apicommon.constant.CommonConstant;
import com.xiaofan.apicommon.domain.po.User;
import com.xiaofan.apicommon.domain.vo.LoginUserVO;
import com.xiaofan.apicommon.domain.vo.UserVO;
import com.xiaofan.apicommon.enums.UserRoleEnum;
import com.xiaofan.apicommon.exception.BusinessException;
import com.xiaofan.apicommon.utils.JsonUtil;
import com.xiaofan.apicommon.utils.MD5Util;
import com.xiaofan.apicommon.utils.RedisUtil;
import com.xiaofan.apicommon.utils.SqlUtils;
import com.xiaofan.user.domain.dto.UserQueryRequest;
import com.xiaofan.user.mapper.UserMapper;
import com.xiaofan.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码（这个不知道哪里会不会用到，先留着）
     */
    public static final String SALT = "xiaofan";

    private static final String USER_LOGIN_STATE="user_login";

    /**
     * 盐值，给登录，注册用的，因为改了原来的逻辑，又不想在数据库新增字段
     */
    public static final String SALT_ = "k1qy7Y8rxaSHJryO";

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            // String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            String encryptPassword = MD5Util.getSaltMD5(userPassword, SALT_);
            // 3. 分配 accessKey, secretKey
            String accessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        //第二次有失败次数及以后才查询缓存
        Object mes = redisUtil.get("user:" + userAccount + ":info");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        if (mes == null) {
            // 比对输入的账号和密码
            queryWrapper.eq("userAccount", userAccount);
            user = this.getOne(queryWrapper);
            // 查不到用户
            if (user == null) {
                log.info("userInfo select fail");
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "查无此用户");
            }
            redisUtil.set("user:" + userAccount + ":info", JsonUtil.obj2String(user), 120);
        }

        //检查用户是否被锁定
        boolean result = checkIsLock(userAccount);
        String key = "user:" + userAccount + ":failCount";
        String jsonUser = (String) redisUtil.get("user:" + userAccount + ":info");
        if (StringUtils.isNotBlank(jsonUser)) {
            user = JsonUtil.string2Obj(jsonUser, User.class);
        }
        boolean saltverifyMD5 = MD5Util.getSaltverifyMD5(userPassword, user.getUserPassword(), SALT_);
        // 密码不匹配
        if (!saltverifyMD5) {
            this.setFailCount(userAccount);
            Object o = redisUtil.get(key);
            int count = (int) o;

            if (count == 5) {
                //判断是否已经达到了最大失败次数,达到就重置
                String lockkey = "user:" + userAccount + ":lockTime";
                redisUtil.set(lockkey, "1", 2 * 60 * 60);//设置锁定时间为2小时


                HashMap<String, String> msg = new HashMap<>();
                // msg.put("phone",user.getPhone());
                msg.put("phone","18818348297");
                msg.put("code","123456");
                // rabbitTemplate.convertAndSend("ali.sms.exchange","ali.verify.code",msg);
                rabbitTemplate.convertAndSend("simple.queue",msg);
                redisUtil.del(key);
                redisUtil.del("user:" + userAccount + ":info");

                throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前账号已经被锁定，请在2小时之后尝试");
            }

            count = 5 - count;

            throw new BusinessException(ErrorCode.PARAMS_ERROR, ("登陆失败，您还剩" + count + "次登录机会"));

        }
        //密码匹配
        redisUtil.del(key);
        redisUtil.del("user:" + userAccount + ":info");

        // String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // // 查询用户是否存在
        // QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // queryWrapper.eq("userAccount", userAccount);
        // queryWrapper.eq("userPassword", encryptPassword);
        // User user = this.baseMapper.selectOne(queryWrapper);
        // // 用户不存在
        // if (user == null) {
        //     log.info("user login failed, userAccount cannot match userPassword");
        //     throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        // }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }



    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    /**
     * 是否被锁定
     *
     * @param userAccount
     * @return
     */
    private boolean checkIsLock(String userAccount) {
        long lockTime = this.getUserLoginTimeLock(userAccount);
        String key = "user:" + userAccount + ":failCount";
        if (lockTime > 0) {//判断用户是否已经被锁定
            String desc = "该账号已经被锁定,请在" + lockTime + "秒之后尝试";
            // return map;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, desc);
        }
        return true;

    }

    /**
     * 设置失败次数
     *
     * @param username username
     */
    private void setFailCount(String username) {
        long count = this.getUserFailCount(username);
        String key = "user:" + username + ":failCount";
        if (count < 0) {//判断redis中是否有该用户的失败登陆次数，如果没有，设置为1，过期时间为2分钟，如果有，则次数+1
            redisUtil.set(key, 1, 120);
        } else {
            redisUtil.incr(key, 1);
        }
    }

    /**
     * 获取当前用户已失败次数
     *
     * @param username username
     * @return 已失败次数
     */
    private int getUserFailCount(String username) {
        String key = "user:" + username + ":failCount";
        //从redis中获取当前用户已失败次数
        Object object = redisUtil.get(key);
        if (object != null) {
            return (int) object;
        } else {
            return -1;
        }
    }

    /**
     * 检查用户是否已经被锁定，如果是，返回剩余锁定时间，如果否，返回-1
     *
     * @param userAccount
     * @return 时间
     */
    private int getUserLoginTimeLock(String userAccount) {
        String key = "user:" + userAccount + ":lockTime";
        int lockTime = (int) redisUtil.getExpireSeconds(key);
        if (lockTime > 0) {//查询用户是否已经被锁定，如果是，返回剩余锁定时间，如果否，返回-1
            return lockTime;
        } else {
            return -1;
        }
    }
}
