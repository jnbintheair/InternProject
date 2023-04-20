package com.availability.ja.controller;

import com.availability.ja.rest.*;
import com.availability.ja.service.UsersService;

import io.swagger.annotations.ApiOperation;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.management.BadAttributeValueExpException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer Authentication")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UsersController {

    @Autowired private UsersService usersService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @PostMapping(value = "/getUserByEmail", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Information", notes = "This method get a user by its email")
    public ResponseEntity<UsersResponse> getUserByEmail(@RequestBody UsersRequest request) throws BadAttributeValueExpException {
        ResponseEntity resp = usersService.getUserByEmail(request);
        return resp;
    }
    @PostMapping(value = "/getUserByName", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Information", notes = "This method get a user by their name")
    public ResponseEntity getUserByName(@RequestBody UsersRequest request) {
        ResponseEntity resp = usersService.getUserByName(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PostMapping(value = "/getPracticeArea", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Practice Area Information", notes = "This method gets a users practice area")
    public ResponseEntity getPracticeArea(@RequestBody UsersRequest request) throws BadAttributeValueExpException {
        ResponseEntity resp = usersService.getPracticeArea(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PostMapping(value = "/getUserDevCenter", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Information", notes = "This method gets a users Dev Center")
    public ResponseEntity getUserDevCenter(@RequestBody UsersRequest request) throws BadAttributeValueExpException {
        ResponseEntity resp = usersService.getDevCenter(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PostMapping(value = "/getAllUsers", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Information", notes = "all users")
    public ResponseEntity<Iterable> getAllUsers() {
        return new ResponseEntity<>(usersService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/createUser", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('MANAGER')")
    @ApiOperation(value = "Creates new User ", notes = "This method creates a user")
    public ResponseEntity createUser(@RequestBody UsersRequest request) throws BadAttributeValueExpException {
        ResponseEntity resp = usersService.createUser(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }

    @PostMapping(value = "/setNewUserCredentials", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('MANAGER')")
    @ApiOperation(value = "Adds information", notes = "This creates credentials")
    public ResponseEntity setNewUserCredentials(@RequestBody NewUsersLoginRequest request) throws Exception {
        ResponseEntity resp = usersService.setUserPassword(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }

    @PostMapping(value = "/setUserCredentials", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "Updates User Credentials", notes = "This creates credentials")
    public  ResponseEntity setUserCredentials(@RequestBody CredentialsRequest request, Authentication authentication) throws Exception {
        ResponseEntity resp = usersService.updateUserPassword(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }

    @PutMapping(value = "/updateUserPassword", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes credential Information", notes = "Changes user password")
    public  ResponseEntity updateUserPassword(@RequestBody CredentialsRequest request, Authentication authentication) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateUserPassword(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateActive", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @ApiOperation(value = "changes activity of user", notes = "Changes activity of user")
    public ResponseEntity updateActive(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateActive(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateFirstName", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes first name of user", notes = "Changes first name of user")
    public  ResponseEntity updateFirstName(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateFirstName(request);
        return new ResponseEntity<>(resp.getBody(),resp.getStatusCode());
    }
    @PutMapping(value = "/updateLastName", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes last name of user", notes = "Changes last name of user")
    public  ResponseEntity updateLastName(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateLastName(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateMiddleName", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes middle name of user", notes = "Changes middle name of user")
    public  ResponseEntity updateMiddleName(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateMiddleName(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateEmail", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @ApiOperation(value = "changes email of user", notes = "Changes email of user")
    public  ResponseEntity updateEmail(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateEmail(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updatePhoneNumber", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes phone number of user", notes = "Changes phone number of user")
    public  ResponseEntity updatePhoneNumber(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updatePhoneNumber(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateGraduated", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes graduation status of user", notes = "Changes graduation status of user")
    public  ResponseEntity updateGraduation(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateGraduated(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateGraduationDate", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes graduation date of user", notes = "Changes graduation date of user")
    public ResponseEntity updateGraduationDate(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateGraduationDate(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateIsManager", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @ApiOperation(value = "changes manager status of user", notes = "Changes manager status of user")
    public  ResponseEntity updateIsManager(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateIsManager(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateDevCenter", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes dev center of user", notes = "Changes dev center of user")
    public  ResponseEntity updateDevCenter(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateDevCenter(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updatePracticeArea", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes practice area of user", notes = "Changes practice area of user")
    public  ResponseEntity updatePracticeArea(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updatePracticeArea(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateTimeZone", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or #request.email == #authentication.name")
    @ApiOperation(value = "changes time zone of user", notes = "Changes time zone of user")
    public  ResponseEntity updateTimeZone(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateTimeZone(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PutMapping(value = "/updateRegion", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "changes region of user", notes = "Changes region of user")
    public  ResponseEntity updateRegion(@RequestBody UsersRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, BadAttributeValueExpException {
        ResponseEntity resp = usersService.updateRegion(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PostMapping(value = "/getPracticeAreaAll", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get List Of Users From Practice Area", notes = "all users")
    public ResponseEntity getPracticeAreaAll(@RequestBody PracticeAreaRequest request) {
        ResponseEntity resp = usersService.getPracticeAreaAll(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PostMapping(value = "/getDevCenterAll", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get List Of Users From DevCenter", notes = "all users")
    public ResponseEntity getDevCenterAll(@RequestBody DevCenterRequest request) {
        ResponseEntity resp = usersService.getDevCenterAll(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PostMapping(value = "/getTimeZoneAll", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get List Of Users From TimeZone", notes = "all users")
    public ResponseEntity getTimeZoneAll(@RequestBody TimeZoneRequest request) {
        ResponseEntity resp = usersService.getTimeZoneAll(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
    @PostMapping(value = "/getRegionAll", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get List Of Users From Region", notes = "all users")
    public ResponseEntity getRegionAll(@RequestBody RegionRequest request) {
        ResponseEntity resp = usersService.getRegionAll(request);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }

    @PostMapping(value = "/getManagersAll", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get List of Managers", notes = "all managers")
    public ResponseEntity<List> getManagersAll(@RequestBody UsersRequest request) {
        return new ResponseEntity<>(usersService.getManagersAll(request), HttpStatus.OK);
    }
    @PutMapping(value = "/setManager", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get List of Managers", notes = "all managers")
    public ResponseEntity getManagersAll(@RequestBody ManagerRequest manager) throws BadAttributeValueExpException {
        ResponseEntity resp = usersService.setManager(manager);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }
}
