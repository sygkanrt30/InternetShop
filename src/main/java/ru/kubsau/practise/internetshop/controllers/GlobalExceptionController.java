package ru.kubsau.practise.internetshop.controllers;

import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionController {
    static String ERROR_WORD = "ОШИБКА:";
    static String DETAILS_WORD = "Подробности:";

    @ExceptionHandler
    public ResponseEntity<String> catchInvalidRequestException(InvalidRequestStateException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<String> catchEntityNotFoundException(EntityNotFoundException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> catchIllegalStateException(IllegalStateException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> catchUsernameNotFoundException(UsernameNotFoundException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<String> getAppErrorHandlerResponseEntity(Exception e, HttpStatus status) {
        String error = e.getMessage();
        log.error(error, e);
        int startIndex = error.indexOf(ERROR_WORD);
        return getResponseEntity(status, startIndex, error);
    }

    private ResponseEntity<String> getResponseEntity(HttpStatus status, int startIndex, String error) {
        if (startIndex != -1) {
            String replacedErrorMessage = getSubStringSqlError(startIndex, error);
            return ResponseEntity.status(status).body("{\"message\":\"" + replacedErrorMessage + "\"}");
        }
        return ResponseEntity.status(status).body("{\"message\":\"" + error + "\"}");
    }

    private String getSubStringSqlError(int startIndex, String error) {
        startIndex += 8;
        int endIndex = error.indexOf(DETAILS_WORD) - 3;
        String errorMessage = error.substring(startIndex, endIndex);
        return errorMessage.replace("\"", "'");
    }
}