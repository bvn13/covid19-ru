package com.bvn13.covid19.model;

import com.bvn13.covid19.model.Covid19ModelConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@SpringBootApplication
//@Import({
//        Covid19ModelConfig.class
//})
public class Covid19ModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19ModelApplication.class, args);
    }

}
