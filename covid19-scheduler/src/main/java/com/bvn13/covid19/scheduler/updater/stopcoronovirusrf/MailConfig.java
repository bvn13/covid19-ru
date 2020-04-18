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

package com.bvn13.covid19.scheduler.updater.stopcoronovirusrf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app.mail")
public class MailConfig {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String host;
    @Min(1)
    @Max(65536)
    private int port;
    @NotBlank
    private String sender;
    @NotBlank
    private String recipient;
    @NotBlank
    private String subject;

    public String constructEndpoint() {
        return "smtp://" + host + ":" + port +
                "?username=" + username +
                "&password=" + password +
                "&from=" + sender +
                "&to=" + recipient +
                "&subject=" + subject;
    }

}
