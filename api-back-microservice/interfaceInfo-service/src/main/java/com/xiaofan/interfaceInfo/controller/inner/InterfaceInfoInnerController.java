package com.xiaofan.interfaceInfo.controller.inner;

/**
 * @Author xiaofan
 * @from <a href="https://github.com/keyne111"></a>
 * @Date 2024/9/18 20:27
 */


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofan.apicommon.domain.po.InterfaceInfo;
import com.xiaofan.feign.client.InterfaceInfoClient;
import com.xiaofan.interfaceInfo.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * 用户提供给别的服务调用的接口
 */
@RestController
@RequestMapping("/inner")
@Slf4j
public class InterfaceInfoInnerController implements InterfaceInfoClient {

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @Override
    @GetMapping("/get/list")
    public List<InterfaceInfo> list(@RequestParam("ids") Collection<Long> ids) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);

        return interfaceInfoService.list(queryWrapper);
    }
}
