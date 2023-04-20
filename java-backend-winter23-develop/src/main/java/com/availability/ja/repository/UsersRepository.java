package com.availability.ja.repository;

import com.availability.ja.model.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<Users,Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByFirstName(String firstName);
    Optional<Users> findByLastName(String lastName);
    @Modifying
    @Transactional
    @Query("update Users E set E.CredentialsID = :CredentialsID where E.userID = :userID")
    void updateCredentialsID(@Param("userID") Long userID, @Param("CredentialsID") long CredentialsID);
    @Modifying
    @Transactional
    @Query("update Users E set E.isManager = :isManager where E.userID = :userID")
    void updateIsManager(@Param("userID") Long userID, @Param("isManager") boolean isManager);
    @Modifying
    @Transactional
    @Query("update Users E set E.isAdmin = :isAdmin where E.userID = :userID")
    void updateIsAdmin(@Param("userID") Long userID, @Param("isAdmin") boolean isAdmin);
    @Modifying
    @Transactional
    @Query("update Users E set E.isActive = :isActive where E.userID = :userID")
    void updateIsActive(@Param("userID") Long userID, @Param("isActive") boolean isActive);
    @Modifying
    @Transactional
    @Query("update Users E set E.practiceAreaID = :PracticeAreaID where E.userID = :userID")
    void updatePracticeAreaID(@Param("userID") Long userID, @Param("PracticeAreaID") Long PracticeAreaID);
    @Modifying
    @Transactional
    @Query("update Users E set E.DevCenterID = :DevCenterID where E.userID = :userID")
    void updateDevCenterID(@Param("userID") Long userID, @Param("DevCenterID") Long DevCenterID);
    @Modifying
    @Transactional
    @Query("update Users E set E.firstName = :firstName where E.userID = :userID")
    void updateFirstName(@Param("userID") Long userID, @Param("firstName") String firstName);
    @Modifying
    @Transactional
    @Query("update Users E set E.lastName = :lastName where E.userID = :userID")
    void updateLastName(@Param("userID") Long userID, @Param("lastName") String lastName);
    @Modifying
    @Transactional
    @Query("update Users E set E.middleName = :middleName where E.userID = :userID")
    void updateMiddleName(@Param("userID") Long userID, @Param("middleName") String middleName);
    @Modifying
    @Transactional
    @Query("update Users E set E.email = :email where E.userID = :userID")
    void updateEmail(@Param("userID") Long userID, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("update Users E set E.phoneNumber = :phoneNumber where E.userID = :userID")
    void updatePhoneNumber(@Param("userID") Long userID, @Param("phoneNumber") String phoneNumber);
    @Modifying
    @Transactional
    @Query("update Users E set E.graduated = :graduated where E.userID = :userID")
    void updateGraduated(@Param("userID") Long userID, @Param("graduated") boolean graduated);
    @Modifying
    @Transactional
    @Query("update Users E set E.graduationDate = :graduationDate where E.userID = :userID")
    void updateGraduationDate(@Param("userID") Long userID, @Param("graduationDate") Date graduationDate);
    @Modifying
    @Transactional
    @Query("update Users E set E.TimeZoneID = :TimeZoneID where E.userID = :userID")
    void updateTimeZoneID(@Param("userID") Long userID, @Param("TimeZoneID") Long TimeZoneID);
    @Modifying
    @Transactional
    @Query("update Users E set E.regionID = :regionID where E.userID = :userID")
    void updateRegionID(@Param("userID") Long userID, @Param("regionID") Long regionID);
    @Modifying
    @Transactional
    @Query("update Users E set E.manager = :manager where E.userID = :userID")
    void updateManager(@Param("userID") Long userID, @Param("manager") String manager);

    @Modifying
    @Transactional
    @Query("update Users E set E.practiceAreaID=:PracticeAreaID," +
            "E.DevCenterID=:DevCenterID," +
            "E.firstName=:firstName," +
            "E.lastName=:lastName," +
            "E.middleName=:middleName," +
            "E.phoneNumber=:phoneNumber," +
            "E.graduated=:graduated," +
            "E.graduationDate=:graduationDate," +
            "E.isActive=:isActive," +
            "E.isAdmin=:isAdmin," +
            "E.isManager=:isManager," +
            "E.TimeZoneID=:TimeZoneID where E.userID=:userID")
    void createUser(@Param("userID") Long userID,
                    @Param("PracticeAreaID") Long PracticeAreaID,
                    @Param("DevCenterID") Long DevCenterID,
                    @Param("firstName") String firstName,
                    @Param("lastName") String lastName,
                    @Param("middleName") String middleName,
                    @Param("phoneNumber") String phoneNumber,
                    @Param("graduated") boolean graduated,
                    @Param("graduationDate") Date graduationDate,
                    @Param("isActive") boolean isActive,
                    @Param("isAdmin") boolean isAdmin,
                    @Param("isManager") boolean isManager,
                    @Param("TimeZoneID") Long TimeZoneID
                    );
}
