package ru.kubsau.practise.internetshop.services.bucket;

import ru.kubsau.practise.internetshop.entities.Bucket;
import ru.kubsau.practise.internetshop.entities.Product;

import java.util.List;

public interface BucketService {
    List<Product> getProductsInBucket(String username);

    void clearAllProductsInBucket(String username);

    void removeAllProductsOfThisTypeFromBucket(String username, long productId);

    void removeProductFromBucket(String username, long productId);

    void createBucket(String username);

    void addProductsToList(Bucket bucket);
}
