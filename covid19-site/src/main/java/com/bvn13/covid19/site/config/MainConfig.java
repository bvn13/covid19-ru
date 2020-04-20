package com.bvn13.covid19.site.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class MainConfig {

    @Value("${app.main-url}")
    private String mainUrl;

}
