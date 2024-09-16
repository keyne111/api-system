package com.xiaofan.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xiaofan.apiclientsdk.domain.po.User;
import com.xiaofan.apiclientsdk.utils.SignUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 利用hutool工具包发送http请求调用controller
 *
 * @author xiaofan
 */


public class ApilClient {
    private static final String GATEWAY_HOST="http://127.0.0.1:8090";

    private String accessKey;
    private String secretKey;

    public ApilClient() {
    }

    public ApilClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result = HttpUtil.get(GATEWAY_HOST+"/api/name/", paramMap);
        return result;
    }

    public String getNameByPost( String name) {

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result = HttpUtil.post(GATEWAY_HOST+"/api/name/", paramMap);
        return result;
    }


    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post(GATEWAY_HOST+"/api/name/user")
                .addHeaders(getHeaderMap(json)) //添加请求头
                .body(json)
                .execute();

        int status = response.getStatus();
        String result = response.body();
        System.out.println("result:"+result);
        return result;

    }

    // 请求头要发送的内容
    private Map<String,String> getHeaderMap(String body){
        Map<String,String> map=new HashMap<>();
        try {
            String encodebody = URLEncoder.encode(body, "UTF-8");

            map.put("accessKey",accessKey);
            //请求体不能放密钥，有被拦截的风险
            // map.put("secretKey",secretKey);
            map.put("body",encodebody); //用户传递的请求体
            map.put("sign", SignUtil.genSign(accessKey,encodebody,secretKey));//签名生成算法  理解成密码加盐操作就行

            //为了保证不被重放，需要随机数，让这个请求只能访问一次，还可以加时间戳，保证随机数过一段时间会被清除
            map.put("nonce", RandomUtil.randomNumbers(4));
            map.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return map;
    }
}
