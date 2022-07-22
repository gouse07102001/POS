package com.increff.pos.controller;
import com.increff.pos.model.MessageData;
import com.increff.pos.service.ApiException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
@ControllerAdvice
public class AppRestControllerAdvice {
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageData handleApiException(ApiException e) {
        return new MessageData(e.getMessage());
    }
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handleTypeMismatch(TypeMismatchException e) {
        return new MessageData("Invalid input. Data types mismatch");
    }
    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handleConversionException(HttpMessageConversionException e) {
        return new MessageData("Invalid request. Could not parse given JSON");
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageData handleException(Exception e) {
        e.printStackTrace();
        return new MessageData(e.getMessage());
    }
}