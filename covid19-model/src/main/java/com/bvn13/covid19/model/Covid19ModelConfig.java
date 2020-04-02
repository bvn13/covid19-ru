package com.bvn13.covid19.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
//@ComponentScan("com.bvn13.covid19.model")
@EntityScan("com.bvn13.covid19.model.entities")
public class Covid19ModelConfig {
}
