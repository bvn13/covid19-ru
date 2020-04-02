package com.bvn13.covid19.scheduler;

import com.bvn13.covid19.model.Covid19ModelConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.bvn13.covid19.scheduler")
@SpringBootApplication
@Import({
        Covid19ModelConfig.class
})
public class Covid19SchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19SchedulerApplication.class, args);
    }

}
