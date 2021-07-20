package com.wenxia.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-11
 */
@SpringBootApplication
@MapperScan("com.wenxia.miaosha.mapper")
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
}
