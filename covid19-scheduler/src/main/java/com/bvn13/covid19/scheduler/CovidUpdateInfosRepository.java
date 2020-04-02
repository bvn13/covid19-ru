package com.bvn13.covid19.scheduler;

import com.bvn13.covid19.model.entities.CovidUpdateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CovidUpdateInfosRepository extends JpaRepository<CovidUpdateInfo, Long> {
}
