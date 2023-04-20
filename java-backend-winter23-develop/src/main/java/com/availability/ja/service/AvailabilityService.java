package com.availability.ja.service;

import com.availability.ja.controller.GlobalExceptionHandler;
import com.availability.ja.model.Users;
import com.availability.ja.model.ReservedWorkHours;
import com.availability.ja.repository.DayOfWeekRepository;
import com.availability.ja.repository.ReservedWorkHoursRepository;
import com.availability.ja.repository.TimeZoneRepository;
import com.availability.ja.repository.UsersRepository;
import com.availability.ja.rest.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import javax.management.BadAttributeValueExpException;
import javax.management.InvalidAttributeValueException;
import javax.management.OperationsException;
import java.sql.SQLDataException;
import java.sql.Time;
import java.util.*;

@Component
public class AvailabilityService {
    @Autowired
    private ReservedWorkHoursRepository reservedWorkHoursRepository;
    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private TimeZoneRepository timeZoneRepository;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    String[][] both = new String[][]{{"06:00:00", "0", "0", "0", "0", "0"}, {"06:30:00", "0", "0", "0", "0", "0"}, {"07:00:00", "0", "0", "0", "0", "0"}, {"07:30:00", "0", "0", "0", "0", "0"}, {"08:00:00", "0", "0", "0", "0", "0"}, {"08:30:00", "0", "0", "0", "0", "0"}, {"09:00:00", "0", "0", "0", "0", "0"}, {"09:30:00", "0", "0", "0", "0", "0"}, {"10:00:00", "0", "0", "0", "0", "0"}, {"10:30:00", "0", "0", "0", "0", "0"}, {"11:00:00", "0", "0", "0", "0", "0"}, {"11:30:00", "0", "0", "0", "0", "0"}, {"12:00:00", "0", "0", "0", "0", "0"}, {"12:30:00", "0", "0", "0", "0", "0"}, {"13:00:00", "0", "0", "0", "0", "0"}, {"13:30:00", "0", "0", "0", "0", "0"}, {"14:00:00", "0", "0", "0", "0", "0"}, {"14:30:00", "0", "0", "0", "0", "0"}, {"15:00:00", "0", "0", "0", "0", "0"}, {"15:30:00", "0", "0", "0", "0", "0"}, {"16:00:00", "0", "0", "0", "0", "0"}, {"16:30:00", "0", "0", "0", "0", "0"}, {"17:00:00", "0", "0", "0", "0", "0"}, {"17:30:00", "0", "0", "0", "0", "0"}, {"18:00:00", "0", "0", "0", "0", "0"}};


    public Iterable<ReservedWorkHours> getHoursAll() {
        Iterable<ReservedWorkHours> results = reservedWorkHoursRepository.findAll();
        return results;
//
    }

    public ArrayList<ReservedWorkHours> getHoursForDay(ReservedWorkHoursRequest request) {
        ArrayList<ReservedWorkHours> HoursList = new ArrayList<>((Collection) getHoursAll());
        ArrayList<ReservedWorkHours> rem = new ArrayList<>();
        for (ReservedWorkHours x : HoursList) {
            if (!(x.getDayOfWeekID() == dayOfWeekRepository.findByDay(request.getDay()))) {
                rem.add(x);
            }
        }
        HoursList.removeAll(rem);
        return HoursList;
    }

    public ArrayList<ReservedWorkHours> getAllForHour(ReservedWorkHoursRequest request) {
        ArrayList<ReservedWorkHours> HoursList = new ArrayList<>((Collection) getHoursAll());
        ArrayList<ReservedWorkHours> rem = new ArrayList<>();
        for (ReservedWorkHours x : HoursList) {
            if ((Objects.equals(dayOfWeekRepository.findByDay(request.getDay()), x.getDayOfWeekID()))) {
                if (Objects.equals(x.getStartTime(), request.getStartTime())) {
                    rem.add(x);

                }
            }
        }
        return rem;
    }

    public ArrayList<ReservedWorkHours> getUserHours(ReservedWorkHoursRequest request) throws BadAttributeValueExpException, InvalidAttributeValueException {
        ArrayList<ReservedWorkHours> HoursList = new ArrayList<>((Collection) getHoursAll());
        ArrayList<ReservedWorkHours> rem = new ArrayList<>();
        Optional<Users> user = usersRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            for (ReservedWorkHours x : HoursList) {
                if (!Objects.equals(x.getUserID(), user.get().getUserID())) {
                    rem.add(x);
                }
            }
            HoursList.removeAll(rem);
            return HoursList;
        } else {
            return null;
        }

    }

    public int checkTimeSlot(ReservedWorkHoursRequest request) throws InvalidAttributeValueException, BadAttributeValueExpException {
        ArrayList<ReservedWorkHours> HoursList = getUserHours(request);
        try {
            for (ReservedWorkHours list : HoursList) {
                if (dayOfWeekRepository.findByDay(request.getDay()) == list.getDayOfWeekID()) {
                    if (request.getStartTime().after(list.getStartTime()) && request.getStartTime().before(list.getEndTime())) {
                        throw new SQLDataException();
                    }
                }
            }
        } catch (SQLDataException F) {
            return 1;
        }
        return 0;
    }

    public ResponseEntity reserveTimeBlock(ReservedWorkHoursRequest request) throws Exception {
        ReservedWorkHours setter = new ReservedWorkHours();
        Optional<Users> user = usersRepository.findByEmail(request.getEmail());
        try {
            if (user.isPresent()) {
                if (((request.getStartTime().after(Time.valueOf("06:00:00"))) || (request.getStartTime().equals(Time.valueOf("06:00:00")))) && ((request.getEndTime().before(Time.valueOf("18:00:00"))) || (request.getEndTime().equals(Time.valueOf("18:00:00"))))) {
                    if (checkTimeSlot(request) == 0) {
                        if (reservedWorkHoursRepository.findByUserHours(request.getStartTime(), request.getEndTime(), dayOfWeekRepository.findByDay(request.getDay()), user.get().getUserID()) == null) {
                            setter.setStartTime(request.getStartTime());
                            setter.setEndTime(request.getEndTime());
                            setter.setUserID(user.get().getUserID());
                            setter.setDayOfWeekID(dayOfWeekRepository.findByDay(request.getDay()));
                            reservedWorkHoursRepository.save(setter);
                        } else {
                            throw new OperationsException("Bad Time Slot Value");
                        }
                    } else {
                        throw new SQLDataException("Time Slot is Taken");
                    }
                }
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (SQLDataException E) {
            return new ResponseEntity(globalExceptionHandler.handleDuplicateInputValue(E), globalExceptionHandler.handleDuplicateInputValue(E).getError());
        } catch (BadAttributeValueExpException F) {
            return new ResponseEntity(globalExceptionHandler.handleBadInputValue(F), globalExceptionHandler.handleBadInputValue(F).getError());
        } catch (OperationsException F) {
            return new ResponseEntity(globalExceptionHandler.handleInvalidInputValue(F), globalExceptionHandler.handleInvalidInputValue(F).getError());
        }
        return null;
    }

    public ResponseEntity<?> updateUserDayHours(UpdateReservedWorkHoursRequest request) throws BadAttributeValueExpException {
        Optional<Users> user = usersRepository.findByEmail(request.getEmail());
        try {
            if (user.isPresent()) {
                reservedWorkHoursRepository.updateStartTime(user.get().getUserID(), request.getNewstartTime(), reservedWorkHoursRepository.findByUserHours(request.getStartTime(), request.getEndTime(), dayOfWeekRepository.findByDay(request.getDay()), user.get().getUserID()));
                reservedWorkHoursRepository.updateEndTime(user.get().getUserID(), request.getNewendTime(), reservedWorkHoursRepository.findByUserHours(request.getStartTime(), request.getEndTime(), dayOfWeekRepository.findByDay(request.getDay()), user.get().getUserID()));

            } else {
                throw new BadAttributeValueExpException(user);
            }
        } catch (BadAttributeValueExpException e) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(e), globalExceptionHandler.handleBadInputValue(e).getError());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity removeTimeSlot(ReservedWorkHoursRequest request) {
        try {
            Optional<Users> user = usersRepository.findByEmail(request.getEmail());
            if (user.isPresent()) {

                reservedWorkHoursRepository.removeTimeSlot(user.get().getUserID(), reservedWorkHoursRepository.findByUserHours(request.getStartTime(), request.getEndTime(), dayOfWeekRepository.findByDay(request.getDay()), user.get().getUserID()));
            } else {
                throw new BadAttributeValueExpException(user);
            }
        } catch (BadAttributeValueExpException e) {
            return new ResponseEntity(globalExceptionHandler.handleBadInputValue(e), globalExceptionHandler.handleBadInputValue(e).getError());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public void setSchedule(ReservedWorkHoursListRequest schedule) throws Exception {
        ArrayList<String> day = new ArrayList<>();
        day.add("monday");
        day.add("tuesday");
        day.add("wednesday");
        day.add("thursday");
        day.add("friday");
        int count = 0;
        for (JsonNode x : schedule.getData()) {
            for (int i = 0; i < x.size(); i++) {
                ReservedWorkHoursRequest request = new ReservedWorkHoursRequest();
                JsonNode y = x.get(i);
                request.setDay(day.get(count));
                request.setStartTime(Time.valueOf(y.findPath("timeBEStart").asText()));
                request.setEndTime(Time.valueOf(y.findPath("timeBEStop").asText()));
                request.setEmail(schedule.getEmail());
                reserveTimeBlock(request);
            }
            count++;
        }

    }

    public ResponseEntity removeSchedule(EmailRequest request) throws Exception {
        ArrayList<ReservedWorkHours> HoursList = new ArrayList<>((Collection) getHoursAll());
        Optional<Users> user = usersRepository.findByEmail(request.getEmail());
        try {
            if (user.isPresent()) {
                for (ReservedWorkHours x : HoursList) {
                    reservedWorkHoursRepository.removeTimeSlot(user.get().getUserID(), reservedWorkHoursRepository.findByUserHours(x.getStartTime(), x.getEndTime(), x.getDayOfWeekID(), user.get().getUserID()));
                }
            } else {
                throw new BadAttributeValueExpException(user);
            }
        } catch (BadAttributeValueExpException e) {
            return new ResponseEntity(globalExceptionHandler.handleBadInputValue(e), globalExceptionHandler.handleBadInputValue(e).getError());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public Boolean threshHold(HourRequest hour) {
        ArrayList<ReservedWorkHours> HoursList = new ArrayList<>((Collection) getHoursAll());
        ArrayList<ReservedWorkHours> rem = new ArrayList<>();
        for (ReservedWorkHours x : HoursList) {
            if ((Objects.equals(dayOfWeekRepository.findByDay(hour.getDay()), x.getDayOfWeekID()))) {
                if (Objects.equals(x.getStartTime(), hour.getHour()) || (x.getStartTime().before(hour.getHour()) && x.getEndTime().after(hour.getHour()))) {
                    rem.add(x);
                }
            }
        }
        Double users = ((Collection<Users>) usersService.getAll()).size() * 1.0;
        double perc = (rem.size() * 1.0) / users;
        if (perc >= .6) {
            return true;
        } else {
            return false;
        }
    }

    public String[][] availabilityList(ArrayList<ReservedWorkHours> hoursList) {
        ArrayList<String> day = new ArrayList<>();
        day.add("monday");
        day.add("tuesday");
        day.add("wednesday");
        day.add("thursday");
        day.add("friday");

        for (int y = 0; y < 5; y++) {
            ArrayList<ReservedWorkHours> rem = new ArrayList<>();

            for (ReservedWorkHours x : hoursList) {
                if (Objects.equals(x.getDayOfWeekID(), dayOfWeekRepository.findByDay(day.get(y)))) {
                    rem.add(x);
                }
            }

            for (int i = 0; i < 25; i++) {
                for (ReservedWorkHours x : rem) {
                    if (Objects.equals(x.getStartTime(), Time.valueOf(both[i][0])) || (x.getStartTime().before(Time.valueOf(both[i][0])) && x.getEndTime().after(Time.valueOf(both[i][0])))) {
                        both[i][y + 1] = String.valueOf(((Integer.valueOf(both[i][y + 1])) + 1));
                    }
                }

            }
            hoursList.removeAll(rem);
        }
        return both;
    }

    public Integer zone(String z) {
        switch (z.toLowerCase()) {
            case "eastern":
                return 0;
            case "central":
                return -1;
            case "mountain":
                return -2;
            case "pacific":
                return -3;
            case "alaska":
                return -4;
            case "hawaii":
                return -5;
        }

        return null;
    }

    public ArrayList<ReservedWorkHours> getHoursOfUser(Long Id) {
        ArrayList<ReservedWorkHours> HoursList = new ArrayList<>((Collection) getHoursAll());
        ArrayList<ReservedWorkHours> rem = new ArrayList<>();

        for (ReservedWorkHours x : HoursList) {
            if (!Objects.equals(x.getUserID(), Id)) {
                rem.add(x);
            }
        }
        HoursList.removeAll(rem);
        return HoursList;
    }

    public ResponseEntity<?> offSetTimeAll(TimeZoneRequest tz) {
        both = new String[][]{{"06:00:00", "0", "0", "0", "0", "0"}, {"06:30:00", "0", "0", "0", "0", "0"}, {"07:00:00", "0", "0", "0", "0", "0"}, {"07:30:00", "0", "0", "0", "0", "0"}, {"08:00:00", "0", "0", "0", "0", "0"}, {"08:30:00", "0", "0", "0", "0", "0"}, {"09:00:00", "0", "0", "0", "0", "0"}, {"09:30:00", "0", "0", "0", "0", "0"}, {"10:00:00", "0", "0", "0", "0", "0"}, {"10:30:00", "0", "0", "0", "0", "0"}, {"11:00:00", "0", "0", "0", "0", "0"}, {"11:30:00", "0", "0", "0", "0", "0"}, {"12:00:00", "0", "0", "0", "0", "0"}, {"12:30:00", "0", "0", "0", "0", "0"}, {"13:00:00", "0", "0", "0", "0", "0"}, {"13:30:00", "0", "0", "0", "0", "0"}, {"14:00:00", "0", "0", "0", "0", "0"}, {"14:30:00", "0", "0", "0", "0", "0"}, {"15:00:00", "0", "0", "0", "0", "0"}, {"15:30:00", "0", "0", "0", "0", "0"}, {"16:00:00", "0", "0", "0", "0", "0"}, {"16:30:00", "0", "0", "0", "0", "0"}, {"17:00:00", "0", "0", "0", "0", "0"}, {"17:30:00", "0", "0", "0", "0", "0"}, {"18:00:00", "0", "0", "0", "0", "0"}};
        ArrayList<ReservedWorkHours> hoursList = new ArrayList<>((Collection) getHoursAll());
        ArrayList<ReservedWorkHours> HoursList = new ArrayList<>();
        ArrayList<Users> user = new ArrayList<>((Collection) usersRepository.findAll());
        for (Users y : user) {
            if (y.getTimeZoneID() != null) {
                ArrayList<ReservedWorkHours> userHours = getHoursOfUser(y.getUserID());
                Optional<com.availability.ja.model.TimeZone> tzone = timeZoneRepository.findById(y.getTimeZoneID());

                if (userHours.size() != 0) {
                    for (ReservedWorkHours x : userHours) {
                        String stime = x.getStartTime().toString();
                        String etime = x.getEndTime().toString();
                        switch (tzone.get().getTimeZone()) {
                            case "Eastern" -> {
                                x.setStartTime(Time.valueOf(stime.replace(stime.substring(0, 2), Integer.toString(Integer.parseInt(stime.substring(0, 2)) + zone(tz.getTimeZone())))));
                                x.setEndTime(Time.valueOf(etime.replace(etime.substring(0, 2), Integer.toString(Integer.parseInt(etime.substring(0, 2)) + zone(tz.getTimeZone())))));
                            }
                            case "Central" -> {
                                x.setStartTime(Time.valueOf(stime.replace(stime.substring(0, 2), Integer.toString(Integer.parseInt(stime.substring(0, 2)) + 1 + zone(tz.getTimeZone())))));
                                x.setEndTime(Time.valueOf(etime.replace(etime.substring(0, 2), Integer.toString(Integer.parseInt(etime.substring(0, 2)) + 1 + zone(tz.getTimeZone())))));
                            }
                            case "Mountain" -> {
                                x.setStartTime(Time.valueOf(stime.replace(stime.substring(0, 2), Integer.toString(Integer.parseInt(stime.substring(0, 2)) + 2 + zone(tz.getTimeZone())))));
                                x.setEndTime(Time.valueOf(etime.replace(etime.substring(0, 2), Integer.toString(Integer.parseInt(etime.substring(0, 2)) + 2 + zone(tz.getTimeZone())))));
                            }
                            case "Pacific" -> {
                                x.setStartTime(Time.valueOf(stime.replace(stime.substring(0, 2), Integer.toString(Integer.parseInt(stime.substring(0, 2)) + 3 + zone(tz.getTimeZone())))));
                                x.setEndTime(Time.valueOf(etime.replace(etime.substring(0, 2), Integer.toString(Integer.parseInt(etime.substring(0, 2)) + 3 + zone(tz.getTimeZone())))));
                            }
                            case "Alaska" -> {
                                x.setStartTime(Time.valueOf(stime.replace(stime.substring(0, 2), Integer.toString(Integer.parseInt(stime.substring(0, 2)) + 4 + zone(tz.getTimeZone())))));
                                x.setEndTime(Time.valueOf(etime.replace(etime.substring(0, 2), Integer.toString(Integer.parseInt(etime.substring(0, 2)) + 4 + zone(tz.getTimeZone())))));
                            }
                            case "Hawaii" -> {
                                x.setStartTime(Time.valueOf(stime.replace(stime.substring(0, 2), Integer.toString(Integer.parseInt(stime.substring(0, 2)) + 5 + zone(tz.getTimeZone())))));
                                x.setEndTime(Time.valueOf(etime.replace(etime.substring(0, 2), Integer.toString(Integer.parseInt(etime.substring(0, 2)) + 5 + zone(tz.getTimeZone())))));
                            }
                        }

                    }
                    hoursList.removeAll(userHours);
                    HoursList.addAll(userHours);
                }

            }
        }
        return new ResponseEntity<>(availabilityList(HoursList), HttpStatus.OK);
    }
}
