package ru.kubsau.practise.internetshop.services.bucket;

import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.entities.Bucket;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.*;

@AllArgsConstructor
@Slf4j
@Service
public class BucketServiceImpl implements BucketService {
    BucketRepository bucketRepository;
    ProductService productService;

    @Override
    public List<Product> getProductsInBucket(String username) {
        String stringOfIds = bucketRepository.getListOfProducts(username);
        String[] listOfIds = stringOfIds.isEmpty() ? new String[0] : stringOfIds.split(",");
        return convertListWithIdToListWithProduct(listOfIds);
    }

    private List<Product> convertListWithIdToListWithProduct(String[] productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            var product = productService.getProductById(Long.parseLong(productId));
            products.add(product);
        }
        return products;
    }

    @Transactional
    @Override
    public void clearAllProductsInBucket(String username) {
        logAndThrowExceptionIfBucketNotFound(username);
        long[] emptyArray = new long[0];
        bucketRepository.updateListOfProductsInBucket(username, emptyArray);
        log.info("All products cleared in bucket with username {}", username);
    }

    private void logAndThrowExceptionIfBucketNotFound(String username) {
        if (bucketRepository.getBucket(username).isEmpty()) {
            log.error("Bucket with username {} not found", username);
            throw new InvalidRequestStateException("Bucket with username " + username + " not found");
        }
    }

    @Transactional
    @Override
    public void removeAllProductsOfThisTypeFromBucket(String username, long productId) {
        Optional<Bucket> bucket = bucketRepository.getBucket(username);
        if (bucket.isPresent()) {
            long[] arr = bucket.get().getProductIds();
            long[] listOfProductIds = Arrays.stream(arr).filter(p -> p != productId).toArray();
            bucketRepository.updateListOfProductsInBucket(username, listOfProductIds);
            return;
        }
        logAndThrowExceptionIfBucketNotFound(username);
    }

    @Transactional
    @Override
    public void removeProductFromBucket(String username, long productId) {
        Optional<Bucket> bucket = bucketRepository.getBucket(username);
        if (bucket.isPresent()) {
            long[] arr = bucket.get().getProductIds();
            replaceIdWithTagForRemoval(productId, arr);
            long[] listOfProductIds = Arrays.stream(arr).filter(p -> p != 0).toArray();
            bucketRepository.updateListOfProductsInBucket(username, listOfProductIds);
            return;
        }
        logAndThrowExceptionIfBucketNotFound(username);
    }

    private void replaceIdWithTagForRemoval(long productId, long[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == productId) {
                arr[i] = 0;
                break;
            }
        }
    }

    @Override
    public void createBucket(String username) {
        bucketRepository.saveBucket(username);
    }

    @Transactional
    @Override
    public void addProductsToList(Bucket bucket) {
        String username = bucket.getUsername();
        Optional<Bucket> optionalBucket = bucketRepository.getBucket(username);
        if (optionalBucket.isPresent()) {
            long[] arrayOfProductIds = addIdsToArray(optionalBucket.get().getProductIds(), bucket.getProductIds());
            bucketRepository.updateListOfProductsInBucket(username, arrayOfProductIds);
            log.info("Added products to bucket with username {}", username);
            return;
        }
        logAndThrowExceptionIfBucketNotFound(username);
    }

    private long[] addIdsToArray(long[] listOfProducts, long[] productIds) {
        int sumOfTwoArrLen = listOfProducts.length + productIds.length;
        long[] arrayOfProductIds = new long[sumOfTwoArrLen];
        System.arraycopy(productIds, 0, arrayOfProductIds, 0, productIds.length);
        System.arraycopy(listOfProducts, 0, arrayOfProductIds, productIds.length, listOfProducts.length);
        return arrayOfProductIds;
    }
}
