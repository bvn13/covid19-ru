package com.bvn13.covid19.api.repositories;

import com.bvn13.covid19.api.dtos.CovidUpdateInfoDto;
import com.bvn13.covid19.model.entities.CovidUpdateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CovidUpdateInfosRepository extends JpaRepository<CovidUpdateInfo, Long> {

    @Query("select new com.bvn13.covid19.api.dtos.CovidUpdateInfoDto(max(U.createdOn)) from CovidUpdateInfo U")
    Optional<CovidUpdateInfoDto> findLastUpdateInfo();

    Optional<CovidUpdateInfo> findFirstByCreatedOn(LocalDateTime createdOn);

}
