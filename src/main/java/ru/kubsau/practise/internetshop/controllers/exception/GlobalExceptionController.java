package ru.kubsau.practise.internetshop.controllers.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppErrorHandler(HttpStatus.NOT_ACCEPTABLE.value(),
                e.getMessage(),
                LocalTime.now()),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<AppErrorHandler> catchEntityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppErrorHandler(HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                LocalTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppErrorHandler> catchIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppErrorHandler(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                LocalTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}