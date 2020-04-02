package com.bvn13.covid19.api;

import com.bvn13.covid19.model.Covid19ModelConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@EnableCaching
@SpringBootApplication
@Import({
        Covid19ModelConfig.class
})
public class Covid19ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19ApiApplication.class, args);
    }

}
