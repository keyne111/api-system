package com.xiaofan.springbootinit.service;


import com.xiaofan.apicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserInterfaceInfoServiceTest {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;


    @Test
    void invokeCount() {
        // boolean result = userInterfaceInfoService.invokeCount(1L, 1L);
        // boolean result = userInterfaceInfoService.isHaveInvoke(1829054989253406721L);
        // Assertions.assertTrue(result);


        // boolean haveInvoke = innerUserInterfaceInfoService.isHaveInvoke(1829054989253406721L);
        // Assertions.assertTrue(haveInvoke);

    }
}