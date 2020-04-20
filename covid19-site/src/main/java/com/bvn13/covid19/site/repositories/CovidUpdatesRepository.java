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

package com.bvn13.covid19.site.repositories;

import com.bvn13.covid19.site.dtos.CovidUpdateInfoDto;
import com.bvn13.covid19.model.entities.CovidUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface CovidUpdatesRepository extends JpaRepository<CovidUpdate, Long> {

    @Query("select new com.bvn13.covid19.site.dtos.CovidUpdateInfoDto(max(U.createdOn)) from CovidUpdate U")
    Optional<CovidUpdateInfoDto> findLastUpdate();

    @Query("select U from CovidUpdate U where U.id in (select max(U1.id) from CovidUpdate U1 group by U1.datetime)")
    Collection<CovidUpdate> findAllLastUpdatesPerDay();

    Optional<CovidUpdate> findFirstByCreatedOn(LocalDateTime createdOn);

}
