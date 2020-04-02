package com.bvn13.covid19.api.service;

import com.bvn13.covid19.api.repositories.CovidStatsRepository;
import com.bvn13.covid19.api.repositories.CovidUpdateInfosRepository;
import com.bvn13.covid19.model.entities.CovidStat;
import com.bvn13.covid19.model.entities.CovidUpdateInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CovidStatsMaker {

    private final CovidStatsRepository covidRepository;
    private final CovidUpdateInfosRepository updatesRepository;

    @Transactional
    public Collection<CovidStat> getLastCovidStats() {
        return updatesRepository.findLastUpdateInfo()
                .flatMap(updateInfo -> updatesRepository.findFirstByCreatedOn(updateInfo.getCreatedOn()))
                .map(covidRepository::findAllByUpdateInfo)
                .orElse(Collections.emptyList());
    }

    @Cacheable(
            cacheNames = "covid-last-update-info",
            unless = "#result == null"
    )
    @Transactional
    public Optional<CovidUpdateInfo> findLastUpdateInfo() {
        return updatesRepository.findLastUpdateInfo()
                .flatMap(updateInfo -> updatesRepository.findFirstByCreatedOn(updateInfo.getCreatedOn()));
    }

    @Cacheable(
            cacheNames = "covid-stats-by-update-info-id",
            condition = "#updateInfoId > 0",
            unless = "#result == null || #result.size() <= 0"
    )
    public Collection<CovidStat> findCovidStatsByUpdateInfoId(long updateInfoId) {
        return covidRepository.findAllByUpdateInfo_Id(updateInfoId);
    }

}
