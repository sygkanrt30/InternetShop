package ru.kubsau.practise.internetshop.services.bucket;

import ru.kubsau.practise.internetshop.model.entities.Product;

import java.util.Map;

public interface BucketService {
    Map<Product, Long> getBucket(String username);

    void clearBucket(String username);

    void removeAllProductsOfThisType(String username, long productId);

    void removeProduct(String username, long productId);

    void create(String username);

    void addProducts(String username, long productId);
}
