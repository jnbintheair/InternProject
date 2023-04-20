package com.availability.ja.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@NoArgsConstructor
@JsonDeserialize
@Data
public class UsersResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("Graduated")
    private boolean graduated;

    @JsonProperty("devCenter")
    private String devCenter;

    @JsonProperty("practiceArea")
    private String practiceArea;

    @JsonProperty("timeZone")
    private String timeZone;

    @JsonProperty("isManager")
    private boolean isManager;

    @JsonProperty("isAdmin")
    private boolean isAdmin;
    @JsonProperty("region")
    private String region;
    @JsonProperty("manager")
    private String managerName;
    @JsonProperty("graduationDate")
    @JsonFormat(pattern = "YYYY-MM-dd" )
    private Date graduationDate;
}
