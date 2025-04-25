package ru.kubsau.practise.internetshop.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kubsau.practise.internetshop.services.exceptions.AppErrorHandler;
import ru.kubsau.practise.internetshop.services.exceptions.InvalidRequestException;

import java.time.LocalTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionController {
    @ExceptionHandler
    public ResponseEntity<AppErrorHandler> catchInvalidRequestException(InvalidRequestException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<AppErrorHandler> catchEntityNotFoundException(EntityNotFoundException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppErrorHandler> catchIllegalStateException(IllegalStateException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<AppErrorHandler> catchUsernameNotFoundException(UsernameNotFoundException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<AppErrorHandler> getAppErrorHandlerResponseEntity(Exception e, HttpStatus status) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppErrorHandler(status.value(),
                e.getMessage(),
                LocalTime.now()),
                status);
    }
}