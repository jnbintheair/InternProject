package com.availability.ja.repository;

import com.availability.ja.model.ReservedWorkHours;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;


@Repository
public interface ReservedWorkHoursRepository extends CrudRepository<ReservedWorkHours,Long> {

    @Query("select f.ReservedWorkHoursID from ReservedWorkHours f where f.startTime = :startTime and f.endTime = :endTime and f.DayOfWeekID = :DayOfWeekID and f.userID = :userID")
    Long findByUserHours(@Param("startTime") Time startTime,@Param("endTime") Time endTime,@Param("DayOfWeekID")Long DayOfWeekID, @Param("userID") Long userID);
    @Modifying
    @Transactional
    @Query("update ReservedWorkHours E set E.startTime = :startTime where E.userID = :userID and E.ReservedWorkHoursID= :ReservedWorkHoursID")
    void updateStartTime(@Param("userID") Long userID, @Param("startTime") Time startTime,@Param("ReservedWorkHoursID")Long ReservedWorkHoursID);
    @Modifying
    @Transactional
    @Query("update ReservedWorkHours E set E.endTime = :endTime where E.userID = :userID and E.ReservedWorkHoursID= :ReservedWorkHoursID")
    void updateEndTime(@Param("userID") Long userID, @Param("endTime") Time endTime,@Param("ReservedWorkHoursID")Long ReservedWorkHoursID);
    @Modifying
    @Transactional
    @Query("delete from ReservedWorkHours E where E.userID = :userID and E.ReservedWorkHoursID= :ReservedWorkHoursID")
    void removeTimeSlot(@Param("userID") Long userID,@Param("ReservedWorkHoursID")Long ReservedWorkHoursID);

}