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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@NoArgsConstructor
@Data
@Slf4j
@ConfigurationProperties(prefix = "app.mail")
public class MailConfig {

    private String username;
    private String password;
    private String host;
    private int port;
    private String sender;
    private String recipient;
    private String subject;
    private boolean debugMode;

    @PostConstruct
    public void init() {
        if (StringUtils.isBlank(host) ||
                StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(sender) ||
                StringUtils.isBlank(recipient) ||
                StringUtils.isBlank(subject) ||
                port <= 0 || port > 65536) {
            throw new IllegalArgumentException("Mail endpoint has no arguments: " + constructEndpoint());
        }

        log.info("MAIL ENDPOINT: " + constructEndpoint());
    }

    public String constructEndpoint() {
        return "smtps://" + host + ":" + port +
                "?username=" + username +
                "&password=" + password +
                "&from=" + sender +
                "&to=" + recipient +
                "&subject=" + subject +
                "&debugMode=" + (debugMode ? "true" : "false");
    }

}
