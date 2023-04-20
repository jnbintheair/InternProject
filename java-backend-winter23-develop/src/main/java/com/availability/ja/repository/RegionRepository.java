package com.availability.ja.repository;

import com.availability.ja.model.Region;
import com.availability.ja.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends CrudRepository<Region,Long> {
    @Query("select f.regionID from Region f where f.region = :region")
    Long findByRegion(@Param("region")String region);
}