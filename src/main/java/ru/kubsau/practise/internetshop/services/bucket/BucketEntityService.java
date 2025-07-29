package ru.kubsau.practise.internetshop.services.bucket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.entities.BucketEntity;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.bucket.exception.BucketNotFoundException;

@RequiredArgsConstructor
@Component
public class BucketEntityService {
    BucketRepository bucketRepository;

    public BucketEntity getBucketOrThrowException(String username) {
        return bucketRepository.findByUsername(username).orElseThrow(
                () -> new BucketNotFoundException("Bucket with name " + username + " not found")
        );
    }
}
