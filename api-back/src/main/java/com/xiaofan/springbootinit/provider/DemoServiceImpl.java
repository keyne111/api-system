package com.xiaofan.springbootinit.provider;

import com.xiaofan.springbootinit.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @Override
    public String sayHello2(String name) {
        return "Hello2 " + name;
    }
}
