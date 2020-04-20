package com.bvn13.covid19.site.service;

import com.bvn13.covid19.model.entities.CovidStat;
import com.bvn13.covid19.site.model.CovidData;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CovidStatsResponseMaker {

    public List<CovidData> convertStats(Collection<CovidStat> stats) {
        return stats.stream()
                .map(stat -> CovidData.builder()
                        .region(stat.getRegion().getName())
                        .sick(stat.getSick())
                        .healed(stat.getHealed())
                        .died(stat.getDied())
                        .build())
                .collect(Collectors.toList());
    }

}
