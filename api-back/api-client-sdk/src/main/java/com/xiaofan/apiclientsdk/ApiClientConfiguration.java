package com.xiaofan.apiclientsdk;


import com.xiaofan.apiclientsdk.client.ApilClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ComponentScan
@ConfigurationProperties("api.client")
public class ApiClientConfiguration {

    private String accessKey;

    private String secretKey;

    @Bean
    public ApilClient apilClient(){
        return new ApilClient(accessKey,secretKey);
    }

}
