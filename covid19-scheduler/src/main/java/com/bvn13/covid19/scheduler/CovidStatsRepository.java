package com.bvn13.covid19.scheduler;

import com.bvn13.covid19.model.entities.CovidStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CovidStatsRepository extends JpaRepository<CovidStat, Long> {

}
