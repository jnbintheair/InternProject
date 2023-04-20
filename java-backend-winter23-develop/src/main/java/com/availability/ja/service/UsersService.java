package com.availability.ja.service;


import com.availability.ja.controller.GlobalExceptionHandler;
import com.availability.ja.model.*;
import com.availability.ja.model.TimeZone;
import com.availability.ja.repository.*;
import com.availability.ja.rest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.management.BadAttributeValueExpException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLDataException;
import java.util.*;

@Component
public class UsersService {
    @Autowired private UsersRepository usersRepository;
    @Autowired private PracticeAreaRepository practiceAreaRepository;
    @Autowired private DevCenterRepository devCenterRepository;
    @Autowired private RegionRepository regionRepository;
    @Autowired private CredentialsRepository credentialRepository;
    @Autowired private TimeZoneRepository timeZoneRepository;
    @Autowired private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private UsersResponse getUsersResponse(Optional<Users> results) throws BadAttributeValueExpException {
        UsersResponse response = new UsersResponse();
        if(results.isPresent()) {
            if (results.get().getFirstName()!=null) {
                Users returnedRow = results.get();
                Optional<PracticeArea> pa_results = practiceAreaRepository.findById(returnedRow.getPracticeAreaID());
                Optional<DevCenter> dc_results = devCenterRepository.findById(returnedRow.getDevCenterID());
                Optional<TimeZone> tz_results = timeZoneRepository.findById(returnedRow.getTimeZoneID());
                response.setFullName(returnedRow.getFirstName() + ' ' + returnedRow.getLastName());
                response.setEmail(returnedRow.getEmail());
                response.setPhoneNumber(returnedRow.getPhoneNumber());
                response.setGraduated(returnedRow.isGraduated());
                response.setPracticeArea(pa_results.get().getPracticeArea());
                response.setTimeZone(tz_results.get().getTimeZone());
                response.setDevCenter(dc_results.get().getDevCenter());
                response.setManager(returnedRow.isManager());
                response.setAdmin(returnedRow.isAdmin());
                response.setRegion(regionRepository.findById(results.get().getRegionID()).get().getRegion());
                response.setManagerName(results.get().getManager());
                response.setGraduationDate(results.get().getGraduationDate());
            }else {
                response.setEmail(results.get().getEmail());
            }
            }
        else {
            throw new BadAttributeValueExpException("User Not Found");
        }
        return response;
    }

    public ResponseEntity getUserByEmail(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());

            return new ResponseEntity<>(getUsersResponse(results), HttpStatus.OK);
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        }
    }

    public ResponseEntity getUserByName(UsersRequest request) {
        try {
            if (request.getFirstName() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            ArrayList<Users> userList = new ArrayList<>((Collection) getAll());
            ArrayList<Users> badUser = new ArrayList<>();
            if (request.getFirstName() != null && request.getLastName() != null) {
                for (Users x : userList) {
                    if (!x.getFirstName().equals(request.getFirstName()) || !x.getLastName().equals(request.getLastName())) {
                        badUser.add(x);
                    }
                }
            } else if (request.getFirstName() != null && request.getLastName() == null) {
                for (Users x : userList) {
                    if (!x.getFirstName().equals(request.getFirstName())) {
                        badUser.add(x);
                    }
                }
            }
            userList.removeAll(badUser);
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        }
    }
    public ResponseEntity getPracticeArea(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            Users returnedRow = results.get();
            Optional<PracticeArea> pa_results = practiceAreaRepository.findById(returnedRow.getPracticeAreaID());
            PracticeAreaResponse response = new PracticeAreaResponse();
            if(pa_results.isPresent()){
                response.setPracticeArea(pa_results.get().getPracticeArea());
            } else {
                throw new BadAttributeValueExpException("User not found");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }
    public ResponseEntity getDevCenter(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            Users returnedRow = results.get();
            Optional<DevCenter> dc_results = devCenterRepository.findById(returnedRow.getDevCenterID());
            DevCenterResponse response = new DevCenterResponse();
            if(dc_results.isPresent()){
                response.setDevCenter(dc_results.get().getDevCenter());
            } else {
                throw new BadAttributeValueExpException("User not found");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    public Iterable<Users> getAll(){
        Iterable<Users> results = usersRepository.findAll();
        return results;
    }

    // admin & manager
    public ResponseEntity createUser(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> userID = usersRepository.findByEmail(request.getEmail());
            if (userID.isPresent()) {
                usersRepository.createUser(userID.get().getUserID(),
                        practiceAreaRepository.findByPracticeArea(request.getPracticeArea()),
                        devCenterRepository.findByDevCenter(request.getDevCenter()),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getMiddleName(),
                        request.getPhoneNumber(),
                        request.isGraduated(),
                        request.getGraduationDate(),
                        true,
                        false,
                        false,
                        timeZoneRepository.findByTimeZone(request.getTimeZone())
                );
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
            return new ResponseEntity<>(null, HttpStatus.OK);
        }  catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin & manager
    public ResponseEntity setUserPassword(NewUsersLoginRequest request) throws Exception {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Credentials creds = new Credentials();
            Users create= new Users();
            if (!jwtUserDetailsService.emailInDB(request.getEmail())){
                if(jwtUserDetailsService.checkEmailDomain(request.getEmail())){
                    String pass =  request.getPword();
                    SecretKey key = PasswordEnDe.generateKey(128);
                    IvParameterSpec ivParameterSpec = PasswordEnDe.generateIv();
                    String algorithm = "AES/CBC/PKCS5Padding";
                    creds.setIv(ivParameterSpec.getIV());
                    creds.setEmail(request.getEmail());
                    creds.setPword(PasswordEnDe.encrypt(algorithm, pass, key, PasswordEnDe.generateIv()));
                    creds.setJkey(key.getEncoded());
                    credentialRepository.save(creds);

                    create.setCredentialsID(creds.getCredentialsID());
                    create.setCognitoUserName(request.getCognitoUserName());
                    create.setEmail(request.getEmail());
                    usersRepository.save(create);
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                else{
                    throw new BadAttributeValueExpException("INVALID_CREDENTIALS");
                }
            }
            else {
                throw new SQLDataException("Account Already Exist");
            }
        }  catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        } catch (SQLDataException E) {
            return new ResponseEntity<>(globalExceptionHandler.handleDuplicateInputValue(E), globalExceptionHandler.handleDuplicateInputValue(E).getError());
        }
    }

    // admin and user
    public ResponseEntity updateUserPassword(CredentialsRequest request) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            Optional<Credentials> ret = credentialRepository.findById(results.get().getCredentialsID());
            if (results.isPresent() && ret.isPresent()) {
                IvParameterSpec ivParameterSpec = PasswordEnDe.generateIv();
                SecretKey key = PasswordEnDe.generateKey(128);
                String algorithm = "AES/CBC/PKCS5Padding";
                credentialRepository.updateKey(ret.get().getCredentialsID(), key.getEncoded());
                credentialRepository.updateIv(ret.get().getCredentialsID(),ivParameterSpec.getIV());
                credentialRepository.updatePassword(results.get().getCredentialsID(),PasswordEnDe.encrypt(algorithm, request.getPword(), key, ivParameterSpec));
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateActive(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateIsActive(results.get().getUserID(), request.isActive());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateIsManager(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateIsManager(results.get().getUserID(), request.isManager());
                usersRepository.updateIsAdmin(results.get().getUserID(), request.isAdmin());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateFirstName(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateFirstName(results.get().getUserID(), request.getFirstName());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateLastName(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateLastName(results.get().getUserID(), request.getLastName());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateMiddleName(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateMiddleName(results.get().getUserID(), request.getMiddleName());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateEmail(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByFirstName(request.getFirstName());
            if (results.isPresent()) {
                usersRepository.updateEmail(results.get().getUserID(), request.getEmail());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin and user
    public ResponseEntity updatePhoneNumber(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updatePhoneNumber(results.get().getUserID(), request.getPhoneNumber());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateGraduated(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateGraduated(results.get().getUserID(), request.isGraduated());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateGraduationDate(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateGraduationDate(results.get().getUserID(), request.getGraduationDate());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateDevCenter(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateDevCenterID(results.get().getUserID(), devCenterRepository.findByDevCenter(request.getDevCenter()));
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updatePracticeArea(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updatePracticeAreaID(results.get().getUserID(), practiceAreaRepository.findByPracticeArea(request.getPracticeArea()));
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateTimeZone(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateTimeZoneID(results.get().getUserID(), timeZoneRepository.findByTimeZone(request.getTimeZone()));
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    // admin
    public ResponseEntity updateRegion(UsersRequest request) throws BadAttributeValueExpException {
        try {
            if (request.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> results = usersRepository.findByEmail(request.getEmail());
            if (results.isPresent()) {
                usersRepository.updateRegionID(results.get().getUserID(), regionRepository.findByRegion(request.getRegion()));
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

    /**public String decUserPassword(String email) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException {
        Optional<Users> results = usersRepository.findByEmail(email);
        Optional<Credentials> ret = credentialRepository.findById(results.get().getCredentialsID());
        IvParameterSpec ivParameterSpec = PasswordEnDe.getIV(ret.get().getIv());
        String algorithm = "AES/CBC/PKCS5Padding";

       return PasswordEnDe.decrypt(algorithm, ret.get().getPword(), new SecretKeySpec(ret.get().getJkey(),"AES"),ivParameterSpec);
    }*/
    public ResponseEntity getPracticeAreaAll(PracticeAreaRequest practiceArea){
        try {
            if (practiceArea.getPracticeArea() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            ArrayList<Users> paList = new ArrayList<>((Collection) getAll());
            ArrayList<Users> rem = new ArrayList<>();
            for (Users x : paList) {
                if (!(x.getPracticeAreaID() == practiceAreaRepository.findByPracticeArea(practiceArea.getPracticeArea()))) {
                    rem.add(x);
                }
            }
            paList.removeAll(rem);
            return new ResponseEntity<>(paList, HttpStatus.OK);
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        }
    }
    public ResponseEntity getTimeZoneAll(TimeZoneRequest timeZone){
        try {
            if (timeZone.getTimeZone() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            ArrayList<Users> tzList = new ArrayList<>((Collection) getAll());
            ArrayList<Users> rem = new ArrayList<>();
            for (Users x : tzList) {
                if (Objects.equals(timeZoneRepository.findByTimeZone(timeZone.getTimeZone()), x.getTimeZoneID())) {
                    rem.add(x);
                }
            }
            return new ResponseEntity<>(rem, HttpStatus.OK);
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        }
    }
    public ResponseEntity getDevCenterAll(DevCenterRequest devCenter){
        try {
            if (devCenter.getDevCenter() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            ArrayList<Users> dcList = new ArrayList<>((Collection) getAll());
            ArrayList<Users> rem = new ArrayList<>();
            for (Users x : dcList) {
                if (!(x.getDevCenterID() == devCenterRepository.findByDevCenter(devCenter.getDevCenter()))) {
                    rem.add(x);
                }
            }
            dcList.removeAll(rem);
            return new ResponseEntity<>(dcList, HttpStatus.OK);
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        }
    }
    public ResponseEntity getRegionAll(RegionRequest region) {
        try {
            if (region.getRegion() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            ArrayList<Users> rList = new ArrayList<>((Collection) getAll());
            ArrayList<Users> rem = new ArrayList<>();
            for (Users x : rList) {
                if (Objects.equals(regionRepository.findByRegion(region.getRegion()), x.getRegionID())) {
                    rem.add(x);
                }
            }
            return new ResponseEntity<>(rem, HttpStatus.OK);
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        }
    }

    public ArrayList<Users> getManagersAll(UsersRequest request) {
        ArrayList<Users> rList = new ArrayList<>((Collection) getAll());
        ArrayList<Users> rem = new ArrayList<>();
        for (Users x : rList) {
            if (x.isManager()) {
                rem.add(x);
            }
        }
        return rem;
    }
    public ResponseEntity setManager(ManagerRequest manager) throws BadAttributeValueExpException {
        try {
            if (manager.getEmail() == null) {
                throw new NullPointerException("Null Input Not Accepted");
            }
            Optional<Users> request = usersRepository.findByEmail(manager.getEmail());
            if (request.isPresent()) {
                usersRepository.updateManager(request.get().getUserID(), manager.getManager());
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                throw new BadAttributeValueExpException("User Not Found");
            }
        } catch (NullPointerException N) {
            return new ResponseEntity<>(globalExceptionHandler.handleNullInputValue(N), globalExceptionHandler.handleNullInputValue(N).getError());
        } catch (BadAttributeValueExpException B) {
            return new ResponseEntity<>(globalExceptionHandler.handleBadInputValue(B), globalExceptionHandler.handleBadInputValue(B).getError());
        }
    }

}
