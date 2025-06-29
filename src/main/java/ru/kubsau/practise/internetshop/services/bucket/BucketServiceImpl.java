package ru.kubsau.practise.internetshop.services.bucket;

import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class BucketServiceImpl implements BucketService {
    BucketRepository bucketRepository;
    ProductService productService;

    @Override
    public Map<Product, Long> getBucket(String username) {
        List<Long> arrOfIds = getProductIds(username);
        return convertToMap(arrOfIds);
    }

    private List<Long> getProductIds(String username) {
        var bucket = getBucketOrThrowException(username);
        var productIds = new ArrayList<Long>();
        Arrays.stream(bucket.getProductIds())
                .forEach(productIds::add);
        return productIds;
    }

    private Bucket getBucketOrThrowException(String username) {
        return bucketRepository.findByUsername(username).orElseThrow(
                () -> new InvalidRequestStateException("Bucket with name " + username + " not found")
        );
    }

    private Map<Product, Long> convertToMap(List<Long> productIds) {
        Map<Product, Long> products = new HashMap<>();
        List<Product> productList = productService.getAllProductsAccordingToIds(productIds);
        productList.forEach(product ->
                products.merge(product, 1L, Long::sum));
        return products;
    }

    @Transactional
    @Override
    public void clearBucket(String username) {
        long[] emptyArray = new long[0];
        bucketRepository.updateBucket(username, emptyArray);
        log.info("All products cleared in bucket with username {}", username);
    }

    @Transactional
    @Override
    public void removeAllProductsOfThisType(String username, long productId) {
        List<Long> ids = getProductIds(username);
        long[] idsWithoutRemote = ids.stream()
                .filter(p -> p != productId)
                .mapToLong(p -> p)
                .toArray();
        bucketRepository.updateBucket(username, idsWithoutRemote);
        log.info("All products of type removed from bucket with username {}", username);
    }

    @Transactional
    @Override
    public void removeProduct(String username, long productId) {
        List<Long> ids = getProductIds(username);
        long[] idsWithoutRemote = deleteOneIdAndConvertToArr(ids, productId);
        bucketRepository.updateBucket(username, idsWithoutRemote);
        log.info("Product with id: {} removed from bucket with username {}", productId, username);
    }

    private long[] deleteOneIdAndConvertToArr(List<Long> ids, long productId) {
        ids.remove(productId);
        return ids.stream()
                .mapToLong(Long::longValue)
                .toArray();
    }

    @Transactional
    @Override
    public void create(Bucket bucket) {
        bucketRepository.save(bucket);
    }

    @Transactional
    @Override
    public void addProducts(String username, long productId) {
        long[] ids = getUpdatedArr(username, productId);
        bucketRepository.updateBucket(username, ids);
        log.info("Added products to bucket with username {}", username);
    }

    private long[] getUpdatedArr(String username, long productId) {
        List<Long> ids = getProductIds(username);
        ids.add(productId);
        return ids.stream()
                .mapToLong(Long::longValue)
                .toArray();
    }
}
