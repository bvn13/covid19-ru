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
import com.bvn13.covid19.model.entities.CovidUpdate;
import com.bvn13.covid19.model.entities.Region;
import com.bvn13.covid19.site.repositories.CovidStatsRepository;
import com.bvn13.covid19.site.repositories.CovidUpdatesRepository;
import com.bvn13.covid19.site.repositories.RegionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CovidStatsMaker {

    private final CovidStatsRepository statsRepository;
    private final CovidUpdatesRepository updatesRepository;
    private final RegionsRepository regionsRepository;

    @Transactional
    public Collection<CovidStat> getLastCovidStats() {
        return updatesRepository.findLastUpdate()
                .flatMap(updateInfo -> updatesRepository.findFirstByCreatedOn(updateInfo.getCreatedOn()))
                .map(statsRepository::findAllByUpdateInfo)
                .orElse(Collections.emptyList());
    }

    @Cacheable(
            cacheNames = "covid-last-update",
            unless = "#result == null"
    )
    @Transactional
    public Optional<CovidUpdate> findLastUpdate() {
        return updatesRepository.findLastUpdate()
                .flatMap(updateInfo -> updatesRepository.findFirstByCreatedOn(updateInfo.getCreatedOn()));
    }

    @Cacheable(
            cacheNames = "covid-stats-by-update-info-id",
            condition = "#updateInfoId > 0",
            unless = "#result == null || #result.size() <= 0"
    )
    public Collection<CovidStat> findCovidStatsByUpdateInfoId(long updateInfoId) {
        return statsRepository.findAllByUpdateInfo_Id(updateInfoId);
    }

    @Cacheable(
            cacheNames = "covid-all-days-updates",
            unless = "#result == null || #result.size() <= 0"
    )
    public Collection<CovidUpdate> findAllLastUpdatesPerDay() {
        return updatesRepository.findAllLastUpdatesPerDay();
    }

    @Cacheable(
            cacheNames = "covid-regions",
            unless = "#result == null || #result.size() <= 0"
    )
    public List<String> findAllRegionsNames() {
        return regionsRepository.findAll().stream().map(Region::getName).collect(Collectors.toList());
    }

}
