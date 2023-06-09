package com.availability.ja.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import java.io.Serializable;

    @NoArgsConstructor
    @JsonDeserialize
    @Data
    public class ErrorResponse implements Serializable {
        private static final long serialVersionUID = 1L;


        @JsonProperty("Error")
        @Valid
        private HttpStatus error;
        @JsonProperty("Message")
        @Valid
        private String message;


    }
