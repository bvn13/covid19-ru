package com.bvn13.covid19.site.controllers;

import com.bvn13.covid19.model.entities.CovidStat;
import com.bvn13.covid19.site.model.CovidAllStats;
import com.bvn13.covid19.site.model.CovidData;
import com.bvn13.covid19.site.model.CovidDayStats;
import com.bvn13.covid19.site.service.CovidStatsMaker;
import com.bvn13.covid19.site.service.CovidStatsResponseMaker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stats")
public class AllStatsController {

    private final CovidStatsMaker covidStatsMaker;
    private final CovidStatsResponseMaker covidStatsResponseMaker;

    @Value("${app.zone-id}")
    private String zoneIdStr;
    private ZoneId zoneId;

    @PostConstruct
    public void init() {
        zoneId = ZoneId.of(zoneIdStr);
    }

    @GetMapping("/all")
    public CovidAllStats getStatistics(@RequestParam("region") String regionName) {

        if (StringUtils.isNotBlank(regionName)) {
            return constructResponseForRegion(regionName);
        } else {
            return constructResponseForAllRegions();
        }

    }

    private CovidAllStats constructResponseForRegion(String regionName) {
        return CovidAllStats.builder()
                .regions(Collections.singletonList(regionName))
                .progress(covidStatsMaker.findAllLastUpdatesPerDay().stream()
                        .map(covidUpdate -> CovidDayStats.builder()
                                .datetime(covidUpdate.getDatetime())
                                .updatedOn(covidUpdate.getCreatedOn().atZone(zoneId))
                                .stats(convertStatsToData(findCovidStatsByUpdateIdAndRegion(covidUpdate.getId(), regionName)))
                                .build())
                        .sorted(CovidDayStats::compareTo)
                        .collect(Collectors.toList()))
                .build();
    }

    private CovidAllStats constructResponseForAllRegions() {
        return CovidAllStats.builder()
                .regions(covidStatsMaker.findAllRegionsNames())
                .progress(covidStatsMaker.findAllLastUpdatesPerDay().stream()
                        .map(covidUpdate -> CovidDayStats.builder()
                                .datetime(covidUpdate.getDatetime())
                                .updatedOn(covidUpdate.getCreatedOn().atZone(zoneId))
                                .stats(convertStatsToData(findCovidStatsByUpdateId(covidUpdate.getId())))
                                .build())
                        .sorted(CovidDayStats::compareTo)
                        .collect(Collectors.toList()))
                .build();
    }

    private List<CovidData> convertStatsToData(Collection<CovidStat> stats) {
        return covidStatsResponseMaker.convertStats(stats);
    }

    private List<CovidStat> findCovidStatsByUpdateIdAndRegion(long updateId, String region) {
        return covidStatsMaker.findCovidStatsByUpdateInfoId(updateId).stream()
                .filter(covidStat -> region.equals(covidStat.getRegion().getName()))
                .collect(Collectors.toList());
    }

    private List<CovidStat> findCovidStatsByUpdateId(long updateId) {
        return new ArrayList<>(covidStatsMaker.findCovidStatsByUpdateInfoId(updateId));
    }

}
