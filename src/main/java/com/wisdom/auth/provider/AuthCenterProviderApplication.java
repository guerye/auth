package com.wisdom.auth.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import tk.mybatis.spring.annotation.MapperScan;

//import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Created by yxs on 2019/1/2. 编译  规则
 */
@SpringBootApplication
@EnableAuthorizationServer
@MapperScan(basePackages = "com.wisdom.auth.provider.mapper.mapper")
public class AuthCenterProviderApplication {
   public static void main(String[] args){
       SpringApplication.run(AuthCenterProviderApplication.class, args);
   }
}
