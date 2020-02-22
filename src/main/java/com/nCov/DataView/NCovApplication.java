package com.nCov.DataView;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * program: NCovApplication
 * description: 入口类
 * author: SoCMo
 * create: 2020/2/21
 */
@SpringBootApplication
@MapperScan("com.nCov.DataView.dao")
public class NCovApplication {

    public static void main(String[] args) {
        SpringApplication.run(NCovApplication.class, args);
    }

}
