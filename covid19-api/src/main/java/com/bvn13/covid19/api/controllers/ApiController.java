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

package com.bvn13.covid19.api.controllers;

import com.bvn13.covid19.api.model.CovidStatsInfo;
import com.bvn13.covid19.api.model.CovidStatsResponse;
import com.bvn13.covid19.api.service.CovidStatsMaker;
import com.bvn13.covid19.model.entities.CovidStat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final CovidStatsMaker covidStatsMaker;

    @Value("${app.zone-id}")
    private String zoneIdStr;
    private ZoneId zoneId;

    @PostConstruct
    public void init() {
        zoneId = ZoneId.of(zoneIdStr);
    }

    @GetMapping
    public CovidStatsResponse getStatistics() {
        return covidStatsMaker.findLastUpdateInfo()
                .map(updateInfo -> CovidStatsResponse.builder()
                        .datetime(updateInfo.getDatetime())
                        .updatedOn(updateInfo.getCreatedOn().atZone(zoneId))
                        .stats(convertStats(covidStatsMaker.findCovidStatsByUpdateInfoId(updateInfo.getId())))
                        .build())
                .orElse(CovidStatsResponse.builder().build());
    }

    private List<CovidStatsInfo> convertStats(Collection<CovidStat> stats) {
        return stats.stream()
                .map(stat -> CovidStatsInfo.builder()
                        .region(stat.getRegion().getName())
                        .sick(stat.getSick())
                        .healed(stat.getHealed())
                        .died(stat.getDied())
                        .build())
                .collect(Collectors.toList());
    }

}
