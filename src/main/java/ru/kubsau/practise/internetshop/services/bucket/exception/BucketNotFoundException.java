package ru.kubsau.practise.internetshop.services.bucket.exception;

public class BucketNotFoundException extends RuntimeException {
    public BucketNotFoundException(String message) {
        super(message);
    }
}
