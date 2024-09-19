package com.xiaofan.user.controller.inner;

/**
 * @Author xiaofan
 * @from <a href="https://github.com/keyne111"></a>
 * @Date 2024/9/18 20:27
 */

import cn.hutool.core.collection.CollUtil;
import com.xiaofan.apicommon.common.ErrorCode;
import com.xiaofan.apicommon.domain.po.User;
import com.xiaofan.apicommon.domain.vo.UserVO;
import com.xiaofan.apicommon.exception.BusinessException;
import com.xiaofan.feign.client.UserClient;
import com.xiaofan.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户提供给别的服务调用的接口
 */
@RestController
@RequestMapping("/inner")
@Slf4j
public class UserInnerController implements UserClient {

    @Resource
    private UserService userService;

    @GetMapping("/get/id")
    @Override
    public User getUserById(@RequestParam("userId") Long userId){
        User user = userService.getById(userId);
        if(user==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return user;
    }

    @GetMapping("/get/ids")
    @Override
    public List<User> listByIds(@RequestParam("ids")  Collection<Long> ids){
        List<User> users = userService.listByIds(ids);
        if(CollUtil.isEmpty(users)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return users;
    }

}
