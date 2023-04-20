package com.availability.ja.controller;

import com.availability.ja.rest.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.management.BadAttributeValueExpException;
import javax.management.OperationsException;
import java.sql.SQLDataException;
import java.util.ArrayList;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            GlobalExceptionHandler.class);
    ErrorResponse errorResponse = new ErrorResponse();
    @ExceptionHandler(JDBCConnectionException.class)
    public String handleConnectionError(Exception ex) {

        LOGGER.error(ex.getMessage(), ex);
        return "connect_error";
    }
    @ExceptionHandler(BadAttributeValueExpException.class)
    public ErrorResponse handleBadInputValue(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        errorResponse.setError(HttpStatus.NOT_FOUND);
        errorResponse.setMessage("User Not Found");
        return errorResponse;
    }
    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse handleNullInputValue(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        errorResponse.setError(HttpStatus.NOT_ACCEPTABLE);
        errorResponse.setMessage("Null Input Not Accepted");
        return errorResponse;
    }
    @ExceptionHandler(SQLDataException.class)
    public ErrorResponse handleDuplicateInputValue(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        errorResponse.setError(HttpStatus.CONFLICT);
        errorResponse.setMessage("Time Slot is Taken");
        return errorResponse;
    }
    @ExceptionHandler(OperationsException.class)
    @ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE,reason = "Bad Time Slot Value")
    public ErrorResponse handleInvalidInputValue(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        errorResponse.setError(HttpStatus.NOT_ACCEPTABLE);
        errorResponse.setMessage("Input Not Accepted");
        return errorResponse;
    }
}
