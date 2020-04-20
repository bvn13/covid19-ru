package com.bvn13.covid19.site;

import com.bvn13.covid19.model.Covid19ModelConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@SpringBootTest
@EnableJpaRepositories("com.bvn13.covid19.scheduler")
@ContextConfiguration(
        classes = { Covid19ModelConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Profile("test")
class Covid19SiteApplicationTests {

    @Test
    void contextLoads() {
    }

}
