package com.bvn13.covid19.site.repositories;

import com.bvn13.covid19.model.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionsRepository extends JpaRepository<Region, Long> {
    Optional<Region> findFirstByName(String name);
}
