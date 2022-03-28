package com.bcs.eduuserboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient // 注册到中心的客户端
@MapperScan("com.bcs.eduuserboot.mapper")
public class EduUserBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduUserBootApplication.class, args);
    }

}
