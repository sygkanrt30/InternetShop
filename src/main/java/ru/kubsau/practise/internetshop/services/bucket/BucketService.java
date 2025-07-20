package ru.kubsau.practise.internetshop.services.bucket;

import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.entities.Bucket;

import java.util.Map;

public interface BucketService {
    Map<ProductResponseDTO, Integer> getBucket(String username);

    void clearBucket(String username);

    void removeAllProductsOfThisType(String username, long productId);

    void removeProduct(String username, long productId);

    void save(Bucket bucket);

    void addProducts(String username, long productId);
}
