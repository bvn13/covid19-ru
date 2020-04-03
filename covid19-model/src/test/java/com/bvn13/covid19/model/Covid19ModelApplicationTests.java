package com.bvn13.covid19.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@SpringBootTest
@ContextConfiguration(
        classes = { Covid19ModelConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Profile("test")
class Covid19ModelApplicationTests {

    @Test
    void contextLoads() {
    }

}
