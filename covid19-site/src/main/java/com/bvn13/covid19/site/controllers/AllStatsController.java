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
import com.bvn13.covid19.site.model.CovidAllStats;
import com.bvn13.covid19.site.model.CovidDayStats;
import com.bvn13.covid19.site.repositories.RegionsRepository;
import com.bvn13.covid19.site.service.StatisticsPreparator;
import com.bvn13.covid19.site.service.StatisticsAggregator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stats")
public class AllStatsController {

    private final StatisticsPreparator statisticsPreparator;
    private final StatisticsAggregator statisticsAggregator;
    private final RegionsRepository regionsRepository;

    @Value("${app.zone-id}")
    private String zoneIdStr;
    private ZoneId zoneId;

    @PostConstruct
    public void init() {
        zoneId = ZoneId.of(zoneIdStr);
    }

    @GetMapping("/all")
    public CovidAllStats getStatistics(@RequestParam(value = "region", required = false) String regionName) {

        if (StringUtils.isNotBlank(regionName)) {
            return constructResponseForRegion(regionName);
        } else {
            return constructResponseForAllRegions();
        }

    }

    private CovidAllStats constructResponseForRegion(String regionName) {
        return regionsRepository.findFirstByName(regionName)
                .map(region -> CovidAllStats.builder()
                        .regions(Collections.singletonList(region.getName()))
                        .progress(compoundProgressForRegions(Collections.singletonList(region)))
                        .build()
                )
                .orElse(CovidAllStats.builder()
                        .regions(Collections.singletonList(regionName))
                        .progress(Collections.emptyList())
                        .build()
                );
    }

    private CovidAllStats constructResponseForAllRegions() {
        return CovidAllStats.builder()
                .regions(statisticsPreparator.findAllRegionsNames())
                .progress(compoundProgressForRegions(regionsRepository.findAll()))
                .build();
    }

    private List<CovidDayStats> compoundProgressForRegions(List<Region> regions) {
        return statisticsPreparator.findAllLastUpdatesPerDay().stream()
                .map(covidUpdate -> prepareDayStats(covidUpdate, regions))
                .sorted(CovidDayStats::compareTo)
                .collect(Collectors.toList());
    }

    private CovidDayStats prepareDayStats(CovidUpdate currentUpdate, List<Region> regions) {
        List<CovidStat> currentStats = findCovidStatsByUpdateIdAndRegion(currentUpdate.getId(), regions);
        Optional<CovidUpdate> prevUpdate = statisticsPreparator.findPrevUpdateByDate(currentUpdate.getDatetime());
        Map<Region, StatsProvider> prevStats = prevUpdate
                .map(covidUpdate -> statisticsPreparator.findCovidStatsByUpdateInfoId(covidUpdate.getId()).stream()
                        .collect(Collectors.toMap(CovidStat::getRegion, (cs) -> (StatsProvider) cs))
                )
                .orElseGet(() -> new HashMap<>(0));

        return CovidDayStats.builder()
                .datetime(currentUpdate.getDatetime())
                .updatedOn(currentUpdate.getCreatedOn().atZone(zoneId))
                .stats(statisticsAggregator.prepareStats(currentStats, prevStats))
                .build();
    }

    private List<CovidStat> findCovidStatsByUpdateIdAndRegion(long updateId, List<Region> regions) {
        return statisticsPreparator.findCovidStatsByUpdateInfoId(updateId).stream()
                .filter(covidStat -> regions.contains(covidStat.getRegion()))
                .collect(Collectors.toList());
    }

}
