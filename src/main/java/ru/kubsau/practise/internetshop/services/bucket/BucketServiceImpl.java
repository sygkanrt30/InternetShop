package ru.kubsau.practise.internetshop.services.bucket;

import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.entities.Bucket;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.*;

@AllArgsConstructor
@Slf4j
@Service
public class BucketServiceImpl implements BucketService {
    BucketRepository bucketRepository;
    ProductService productService;

    @Override
    public Map<Product, Long> getProductsInBucket(String username) {
        String stringOfIds = bucketRepository.getListOfProducts(username);
        String[] listOfIds = stringOfIds.isEmpty() ? new String[0] : stringOfIds.split(",");
        return convertArrWithIdToMapWithProduct(listOfIds);
    }

    private Map<Product, Long> convertArrWithIdToMapWithProduct(String[] productIds) {
        Map<Product, Long> products = new HashMap<>();
        for (String productId : productIds) {
            var product = productService.getProductById(Long.parseLong(productId));
            putProductInMap(products, product);
        }
        return products;
    }

    private void putProductInMap(Map<Product, Long> products, Product product) {
        if (products.containsKey(product)) {
            products.put(product, products.get(product) + 1);
        } else {
            products.putIfAbsent(product, 1L);
        }
    }

    @Transactional
    @Override
    public void clearAllProductsInBucket(String username) {
        getBucketOrThrowException(username);
        long[] emptyArray = new long[0];
        bucketRepository.updateListOfProductsInBucket(username, emptyArray);
        log.info("All products cleared in bucket with username {}", username);
    }

    private Bucket getBucketOrThrowException(String username) {
        return bucketRepository.getBucket(username).orElseThrow(
                () -> new InvalidRequestStateException("Bucket with name " + username + " not found")
        );
    }

    @Transactional
    @Override
    public void removeAllProductsOfThisTypeFromBucket(String username, long productId) {
        Bucket bucket = getBucketOrThrowException(username);
        long[] arr = bucket.getProductIds();
        long[] arrWithoutRemoteId = Arrays.stream(arr)
                .filter(p -> p != productId)
                .toArray();
        bucketRepository.updateListOfProductsInBucket(username, arrWithoutRemoteId);
        log.info("All products of type removed from bucket with username {}", username);
    }

    @Transactional
    @Override
    public void removeProductFromBucket(String username, long productId) {
        Bucket bucket = getBucketOrThrowException(username);
        long[] arr = bucket.getProductIds();
        long[] arrWithoutRemoteId = deleteOneIdFromArray(arr, productId);
        bucketRepository.updateListOfProductsInBucket(username, arrWithoutRemoteId);
        log.info("Product with id: {} removed from bucket with username {}", productId, username);
    }

    private long[] deleteOneIdFromArray(long[] arr, long productId) {
        long[] arrWithoutRemoteId = new long[arr.length];
        var flag = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == productId && !flag) {
                flag = true;
                continue;
            }
            arrWithoutRemoteId[i] = arr[i];
        }
        return Arrays.stream(arrWithoutRemoteId).filter(p -> p != 0).toArray();
    }

    @Override
    public void createBucket(String username) {
        bucketRepository.saveBucket(username);
    }

    @Transactional
    @Override
    public void addProductsToList(String username, long productId) {
        Bucket bucket = getBucketOrThrowException(username);
        long[] oneIdArr = new long[]{productId};
        long[] arrayOfProductIds = addIdsToArray(bucket.getProductIds(), oneIdArr);
        bucketRepository.updateListOfProductsInBucket(username, arrayOfProductIds);
        log.info("Added products to bucket with username {}", username);
    }

    private long[] addIdsToArray(long[] oldListOfProducts, long[] newProducts) {
        int oldLstLents = oldListOfProducts.length;
        int newLstLents = newProducts.length;
        long[] arrayOfProductIds = new long[oldLstLents + newLstLents];
        System.arraycopy(newProducts, 0, arrayOfProductIds, 0, newLstLents);
        System.arraycopy(oldListOfProducts, 0, arrayOfProductIds, newLstLents, oldLstLents);
        return arrayOfProductIds;
    }
}
