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

import com.bvn13.covid19.scheduler.updater.stopcoronovirusrf.model.RowData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class StopcoronovirusRfUpdater extends RouteBuilder {

    public static final String HEADER_DATE_OF_DATA = "covid19-date-of-data";

    private final StopcoronovirusRfDataRetriever dataRetriever;
    private final StopcoronovirusRfService service;

    @Value("${app.timer.stopcoronovirusrf}")
    private int stopcoronovirusrfTimerSecons;
    private long stopcoronovirusrfTimerMillis;


    @PostConstruct
    public void init() {
        stopcoronovirusrfTimerMillis = stopcoronovirusrfTimerSecons * 1000;
    }

    @Override
    public void configure() throws Exception {

        from("timer:stopcoronovirusrf?delay=1000&period="+stopcoronovirusrfTimerMillis)
                .log(LoggingLevel.DEBUG, log, "Start retrieving data from stopcoronovirus.rf")
                .process(dataRetriever::retrieveData)
                .bean(this, "saveData")
                .log(LoggingLevel.DEBUG, log, "Processed data count: ${body.size}")
        ;

    }

    @Handler
    public void saveData(@Header(HEADER_DATE_OF_DATA) String dataDate, @Body Collection<RowData> rawData) {
        service.saveRawData(dataDate, rawData);
    }

}
