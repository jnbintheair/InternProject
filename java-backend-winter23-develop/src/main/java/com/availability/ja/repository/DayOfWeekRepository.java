package com.availability.ja.repository;
import com.availability.ja.model.DayOfWeek;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DayOfWeekRepository extends CrudRepository<DayOfWeek,Long> {
    @Query("select f.DayOfWeekID from DayOfWeek f where f.Day = :Day")
    Long findByDay(@Param("Day") String Day);

}