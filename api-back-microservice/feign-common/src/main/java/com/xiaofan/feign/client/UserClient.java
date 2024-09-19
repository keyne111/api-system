package com.xiaofan.feign.client;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofan.apicommon.common.BaseResponse;
import com.xiaofan.apicommon.common.ErrorCode;
import com.xiaofan.apicommon.domain.po.User;
import com.xiaofan.apicommon.domain.vo.LoginUserVO;
import com.xiaofan.apicommon.domain.vo.UserVO;
import com.xiaofan.apicommon.enums.UserRoleEnum;
import com.xiaofan.apicommon.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * @Author xiaofan
 * @from <a href="https://github.com/keyne111"></a>
 * @Date 2024/9/17 16:31
 */
@FeignClient(value = "user-service",path = "/api/user/inner")
public interface UserClient {

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute("user_login");
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(@RequestBody User user){
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    default UserVO getUserVO(@RequestBody User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @GetMapping("/get/id")
    User getUserById(@RequestParam("userId") Long userId);


    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("ids")  Collection<Long> ids);




}
