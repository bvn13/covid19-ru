package com.bvn13.covid19.api.repositories;

import com.bvn13.covid19.model.entities.CovidStat;
import com.bvn13.covid19.model.entities.CovidUpdateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CovidStatsRepository extends JpaRepository<CovidStat, Long> {

    Collection<CovidStat> findAllByUpdateInfo(CovidUpdateInfo updateInfo);
    Collection<CovidStat> findAllByUpdateInfo_Id(long updateInfoId);

}
