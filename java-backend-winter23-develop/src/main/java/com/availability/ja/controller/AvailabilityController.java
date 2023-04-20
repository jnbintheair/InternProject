package com.availability.ja.controller;



import com.availability.ja.model.ReservedWorkHours;
import com.availability.ja.rest.*;
import com.availability.ja.service.AvailabilityService;
import com.availability.ja.service.UsersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import javax.management.BadAttributeValueExpException;
import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/hours")
@SecurityRequirement(name = "Bearer Authentication")
public class AvailabilityController {
    @Autowired private AvailabilityService availabilityService;
    @Autowired private UsersService usersService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @PostMapping(value = "/reserveTimeBlock", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reserveTimeBlock(@RequestBody ReservedWorkHoursRequest request) throws Exception {
        ResponseEntity resp =availabilityService.reserveTimeBlock(request);
        return new ResponseEntity<>(resp.getBody(),resp.getStatusCode());
    }
    @PutMapping(value = "/updateUserDayHours", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserDayHours(@RequestBody UpdateReservedWorkHoursRequest request) throws BadAttributeValueExpException {
        ResponseEntity resp = availabilityService.updateUserDayHours(request);
        return new ResponseEntity<>(resp.getBody(),resp.getStatusCode());
    }
    @PostMapping(value = "/getHoursForDay", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ArrayList<ReservedWorkHours> getHoursForDay(@RequestBody ReservedWorkHoursRequest request) {
       return availabilityService.getHoursForDay(request);
    }
    @PostMapping(value = "/getAllForHour", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getAllForHour(@RequestBody ReservedWorkHoursRequest request){
        return new ResponseEntity<>(availabilityService.getAllForHour(request), HttpStatus.OK);
    }
    @PostMapping(value = "/getUserHours", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserHours(@RequestBody ReservedWorkHoursRequest request) throws BadAttributeValueExpException, InvalidAttributeValueException {
        if(availabilityService.getUserHours(request) != null) {
            return new ResponseEntity<>(availabilityService.getUserHours(request),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(new BadAttributeValueExpException("")),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(value = "/removeTimeSlot", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity removeTimeSlot(@RequestBody ReservedWorkHoursRequest request){
        ResponseEntity resp = availabilityService.removeTimeSlot(request);
        return new ResponseEntity(resp.getBody(),resp.getStatusCode());
    }
    @PostMapping("/setSchedule")
    public void setSchedule(@RequestBody ReservedWorkHoursListRequest List) throws Exception {
        availabilityService.setSchedule(List);
    }
    @DeleteMapping("/removeSchedule")
    public ResponseEntity removeSchedule(@RequestBody EmailRequest email) throws Exception {
        ResponseEntity resp = availabilityService.removeSchedule(email);
        return new ResponseEntity(resp.getBody(),resp.getStatusCode());
    }
    @GetMapping("/getAllUsersSize")
    public int getAllUsersSize(){
        ArrayList<Object> list = new ArrayList<>((Collection) usersService.getAll());
        return list.size();
    }
    @PostMapping("/getThreshHold")
    public Boolean getThreshHold(@RequestBody HourRequest hour){
        return availabilityService.threshHold(hour);
    }
    @PostMapping("/offSetTimeAll")
    public ResponseEntity<?> offSetTimeAll(@RequestBody TimeZoneRequest tz){
        if(availabilityService.zone(tz.getTimeZone())==null){
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(new NullPointerException()),globalExceptionHandler.handleNullInputValue(new NullPointerException()).getError());
        }
        return new ResponseEntity<>(availabilityService.offSetTimeAll(tz),HttpStatus.OK).getBody();
    }
}
