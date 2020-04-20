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

package com.bvn13.covid19.site.service;

import com.bvn13.covid19.model.entities.CovidStat;
import com.bvn13.covid19.model.entities.Region;
import com.bvn13.covid19.model.entities.StatsProvider;
import com.bvn13.covid19.site.model.CovidData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class StatisticsAggregator {

    private static final StatsProvider zero = CovidData.Delta.builder()
            .sick(0L)
            .healed(0L)
            .died(0L)
            .build();


    public List<CovidData> prepareStats(Collection<CovidStat> current, Map<Region, StatsProvider> previous) {
        return current.stream()
                .map(stat -> Pair.of(stat, Optional.ofNullable(previous.get(stat.getRegion()))))
                .map(statPair -> CovidData.builder()
                        .region(statPair.getFirst().getRegion().getName())
                        .sick(statPair.getFirst().getSick())
                        .healed(statPair.getFirst().getHealed())
                        .died(statPair.getFirst().getDied())
                        .previous(CovidData.Delta.builder()
                                .sick(statPair.getSecond().orElse(zero).getSick())
                                .healed(statPair.getSecond().orElse(zero).getHealed())
                                .died(statPair.getSecond().orElse(zero).getDied())
                                .build()
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }

}
