package com.xiaofan.apiclientsdk.utils;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * APi签名生成算法
 */
public class SignUtil {


    //签名生成算法 理解成对secretKey密码加盐操作就行
    public static  String genSign(String accessKey, String body, String secretKey) {
        String md5Hex1 = DigestUtil.md5Hex(accessKey+"-"+body+"-"+secretKey);
        return md5Hex1;
    }
}
