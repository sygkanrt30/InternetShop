package ru.kubsau.practise.internetshop.services.exceptions;

import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
