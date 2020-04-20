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

package com.bvn13.covid19.site.controllers;

import com.bvn13.covid19.site.model.CovidDayStats;
import com.bvn13.covid19.site.service.CovidStatsMaker;
import com.bvn13.covid19.site.service.CovidStatsResponseMaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.ZoneId;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stats")
public class LastStatsController {

    private final CovidStatsMaker covidStatsMaker;
    private final CovidStatsResponseMaker covidStatsResponseMaker;

    @Value("${app.zone-id}")
    private String zoneIdStr;
    private ZoneId zoneId;

    @PostConstruct
    public void init() {
        zoneId = ZoneId.of(zoneIdStr);
    }

    @GetMapping("/last")
    public CovidDayStats getStatistics() {
        return covidStatsMaker.findLastUpdate()
                .map(covidUpdate -> CovidDayStats.builder()
                        .datetime(covidUpdate.getDatetime())
                        .updatedOn(covidUpdate.getCreatedOn().atZone(zoneId))
                        .stats(covidStatsResponseMaker.convertStats(covidStatsMaker.findCovidStatsByUpdateInfoId(covidUpdate.getId())))
                        .build())
                .orElse(CovidDayStats.builder().build());
    }

}
