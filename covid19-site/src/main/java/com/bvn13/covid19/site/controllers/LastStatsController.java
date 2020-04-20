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

import com.bvn13.covid19.model.entities.CovidStat;
import com.bvn13.covid19.model.entities.CovidUpdate;
import com.bvn13.covid19.model.entities.Region;
import com.bvn13.covid19.model.entities.StatsProvider;
import com.bvn13.covid19.site.model.CovidDayStats;
import com.bvn13.covid19.site.service.StatisticsPreparator;
import com.bvn13.covid19.site.service.StatisticsAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stats")
public class LastStatsController {

    private final StatisticsPreparator statisticsPreparator;
    private final StatisticsAggregator statisticsAggregator;

    @Value("${app.zone-id}")
    private String zoneIdStr;
    private ZoneId zoneId;

    @PostConstruct
    public void init() {
        zoneId = ZoneId.of(zoneIdStr);
    }

    @GetMapping("/last")
    public CovidDayStats getStatistics() {
        Optional<CovidUpdate> lastUpdate = statisticsPreparator.findLastUpdate();
        if (lastUpdate.isPresent()) {
            Collection<CovidStat> currentStats = statisticsPreparator.findCovidStatsByUpdateInfoId(lastUpdate.get().getId());
            Optional<CovidUpdate> prevUpdate = statisticsPreparator.findPrevUpdateByDate(lastUpdate.get().getDatetime());
            Map<Region, StatsProvider> prevStats = prevUpdate
                    .map(covidUpdate -> statisticsPreparator.findCovidStatsByUpdateInfoId(covidUpdate.getId()).stream()
                            .collect(Collectors.toMap(CovidStat::getRegion, (cs) -> (StatsProvider) cs))
                    )
                    .orElseGet(() -> new HashMap<>(0));

            return CovidDayStats.builder()
                    .datetime(lastUpdate.get().getDatetime())
                    .updatedOn(lastUpdate.get().getCreatedOn().atZone(zoneId))
                    .stats(statisticsAggregator.prepareStats(currentStats, prevStats))
                    .build();
        } else {
            return CovidDayStats.builder().build();
        }
    }

}
