/*
Copyright [2020] [bvn13]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.bvn13.covid19.scheduler;

import com.bvn13.covid19.model.Covid19ModelConfig;
import com.bvn13.covid19.scheduler.updater.stopcoronovirusrf.MailConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;

@EnableJpaRepositories("com.bvn13.covid19.scheduler")
@SpringBootApplication
@Import({
        Covid19ModelConfig.class
})
@EnableConfigurationProperties(MailConfig.class)
public class Covid19SchedulerApplication {

    public static void main(String[] args) {

        String geckoFilename = ResourceManager.extract("/files/geckodriver");
        Assert.notNull(geckoFilename, "geckodriver not found inside JAR");

        File file = new File(geckoFilename);
        if (!file.canExecute()) {
            Assert.isTrue(file.setExecutable(true, true), "Could not make executable: "+geckoFilename);
        }

        System.setProperty("webdriver.gecko.driver", geckoFilename);

        SpringApplication.run(Covid19SchedulerApplication.class, args);
    }

}
