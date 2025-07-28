package ru.kubsau.practise.internetshop.services.bucket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.bucket.exception.BucketNotFoundException;

@RequiredArgsConstructor
@Component
public class BucketInnerService {
    BucketRepository bucketRepository;

    public Bucket getBucketOrThrowException(String username) {
        return bucketRepository.findByUsername(username).orElseThrow(
                () -> new BucketNotFoundException("Bucket with name " + username + " not found")
        );
    }
}
