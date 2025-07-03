package ru.kubsau.practise.internetshop.services.bucket;

import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.ProductMapper;
import ru.kubsau.practise.internetshop.model.entities.Bucket;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BucketServiceImpl implements BucketService {
    BucketRepository bucketRepository;
    ProductService productService;
    ProductMapper productMapper;

    @Override
    public Map<ProductResponseDTO, Integer> getBucket(String username) {
        return getMapWithDtoKeys(username);
    }

    private Map<ProductResponseDTO, Integer> getMapWithDtoKeys(String username) {
        Map<Long, Integer> productQuantities = getBucketOrThrowException(username).getProducts();
        var productIds = productQuantities.keySet();
        Map<Long, Product> productsMap = productService.getProductsAccordingToIds(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        return productQuantities.entrySet().stream()
                .filter(entry -> productsMap.containsKey(entry.getKey()))
                .collect(Collectors.toMap(
                        entry -> productMapper.toDto(productsMap.get(entry.getKey())),
                        Map.Entry::getValue
                ));
    }

    private Bucket getBucketOrThrowException(String username) {
        return bucketRepository.findByUsername(username).orElseThrow(
                () -> new InvalidRequestStateException("Bucket with name " + username + " not found")
        );
    }

    @Transactional
    @Override
    public void clearBucket(String username) {
        var bucket = getBucketOrThrowException(username);
        var emptyMap = new HashMap<Long, Integer>();
        bucket.setProducts(emptyMap);
        save(bucket);
        log.info("All products cleared in bucket with username {}", username);
    }

    @Transactional
    @Override
    public void removeAllProductsOfThisType(String username, long productId) {
        var bucket = getBucketOrThrowException(username);
        bucket.getProducts().remove(productId);
        save(bucket);
        log.info("All products of type removed from bucket with username {}", username);
    }

    @Transactional
    @Override
    public void removeProduct(String username, long productId) {
        var bucket = getBucketOrThrowException(username);
        Map<Long, Integer> map = bucket.getProducts();
        map.computeIfPresent(productId, (k, quantity) -> quantity > 1 ? quantity - 1 : null);
        save(bucket);
        log.info("Product with id: {} removed from bucket with username {}", productId, username);
    }

    @Transactional
    @Override
    public void save(Bucket bucket) {
        bucketRepository.save(bucket);
    }

    @Transactional
    @Override
    public void addProducts(String username, long productId) {
        var bucket = getBucketOrThrowException(username);
        Map<Long, Integer> map = bucket.getProducts();
        map.merge(productId, 1, Integer::sum);
        save(bucket);
        log.info("Added products to bucket with username {}", username);
    }
}
