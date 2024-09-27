package com.xiaofan.apiinterface.controller;


import com.xiaofan.apiclientsdk.domain.po.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestController
@RequestMapping("/")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name){
        return "GET 你的名字是:"+name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name){
        return "Post 你的名字是:"+name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request,@RequestHeader(value = "source",required = true)String source ){
        // String accessKey = request.getHeader("accessKey");
        // String body = request.getHeader("body");
        // String sign = request.getHeader("sign");
        // String nonce = request.getHeader("nonce");
        // String timestamp = request.getHeader("timestamp");
        //
        // // 校验参数 这里应该去根据accessKey查数据库
        // if(!accessKey.equals("xiaofan") ){
        //     throw new RuntimeException("无权限");
        // }
        // //应该是存到数据库，如果随机数和数据库中不一样
        // if(Long.parseLong(nonce)>10000){
        //     throw new RuntimeException("无权限");
        // }
        // String checkSign = SignUtil.genSign(accessKey, body, "abcdefgh");
        // if(!sign.equals(checkSign)){
        //     throw new RuntimeException("无权限");
        // }
        // //时间和当前时间不能超过5分钟
        // Long currentTime = System.currentTimeMillis() / 1000;
        // final Long FIVE_MINUTES= 60 * 5l;
        // if((currentTime-Long.parseLong(timestamp))>=FIVE_MINUTES){
        //     throw new RuntimeException("无权限");
        // }

        System.out.println("source是:"+source);
        return "POST 你的名字是:"+user.getUserName();
    }
    public static boolean isValidTimestamp(String timestampStr) {
        try {
            // 获取当前时间的时间戳（秒为单位）
            long currentTimestamp = Instant.now().getEpochSecond();

            // 将请求中的时间戳字符串转换为 long 类型
            long requestTimestamp = Long.parseLong(timestampStr);

            // 计算时间差，取绝对值
            long timeDifference = Math.abs(currentTimestamp - requestTimestamp);

            // 设置时间差的阈值为5分钟（300秒）
            long maxDifference = 300;

            // 比较时间差是否在限定范围内
            if (timeDifference <= maxDifference) {
                return true;
            }
        } catch (NumberFormatException e) {
            // 处理时间戳转换异常
            e.printStackTrace();
        }
        return false;
    }

}
