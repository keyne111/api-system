package com.xiaofan.apiinterface;

import com.xiaofan.apiclientsdk.client.ApilClient;
import com.xiaofan.apiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    @Resource
    private ApilClient apilClient;

    @Test
    void contextLoads() {

        String result = apilClient.getNameByGet("xiaofan");
        User user = new User();
        user.setUserName("asdaljk");
        apilClient.getUserNameByPost(user);

    }

}
