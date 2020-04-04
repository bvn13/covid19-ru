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
