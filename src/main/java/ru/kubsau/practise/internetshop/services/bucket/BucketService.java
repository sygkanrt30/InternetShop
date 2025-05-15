package ru.kubsau.practise.internetshop.services.bucket;

import ru.kubsau.practise.internetshop.entities.Product;

import java.util.Map;

public interface BucketService {
    Map<Product, Long> getProductsInBucket(String username);

    void clearAllProductsInBucket(String username);

    void removeAllProductsOfThisTypeFromBucket(String username, long productId);

    void removeProductFromBucket(String username, long productId);

    void createBucket(String username);

    void addProductsToList(String username, long productId);
}
