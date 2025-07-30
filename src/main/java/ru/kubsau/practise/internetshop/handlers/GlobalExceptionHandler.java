package ru.kubsau.practise.internetshop.handlers;

import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTOCreator;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseDTO catchInvalidRequestException(InvalidRequestStateException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseDTO catchEntityNotFoundException(EntityNotFoundException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseDTO catchIllegalStateException(IllegalStateException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseDTO catchUsernameNotFoundException(UsernameNotFoundException e) {
        return getAppErrorHandlerResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    private ResponseDTO getAppErrorHandlerResponseEntity(Exception e, HttpStatus status) {
        String error = e.getMessage();
        log.error(error, e);
        return ResponseDTOCreator.getResponseError(status, error);
    }
}