package ru.kubsau.practise.internetshop.services.bucket.exception;

import lombok.Getter;

@Getter
public class BucketNotFoundException extends RuntimeException {
    public BucketNotFoundException(String message) {
        super(message);
    }
}
