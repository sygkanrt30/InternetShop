package ru.kubsau.practise.internetshop.model.dto;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class ResponseDTOCreator {
    public ResponseDTO getResponseOK(){
        var status = HttpStatus.OK;
        return new ResponseDTO(status, status.getReasonPhrase());
    }
    public ResponseDTO getResponseError(HttpStatus status, String errorMessage) {
        return new ResponseDTO(status, errorMessage);
    }
}
