package ru.kubsau.practise.internetshop.services.bucket;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.ProductMapper;
import ru.kubsau.practise.internetshop.model.entities.BucketEntity;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BucketServiceImpl implements BucketService {
    BucketRepository bucketRepository;
    ProductService productService;
    ProductMapper productMapper;
    BucketEntityService bucketInnerService;

    @Override
    public Map<ProductResponseDTO, Integer> getBucket(String username) {
        return getMapWithDtoKeys(username);
    }

    private Map<ProductResponseDTO, Integer> getMapWithDtoKeys(String username) {
        Map<Long, Integer> productQuantities = bucketInnerService.getBucketOrThrowException(username).getProducts();
        Map<Long, Product> productsByIds = productService.getProductsByIds(productQuantities.keySet());
        return productQuantities.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> productMapper.toDto(productsByIds.get(entry.getKey())),
                        Map.Entry::getValue
                ));
    }

    @Transactional
    @Override
    public void clearBucket(String username) {
        var bucket = bucketInnerService.getBucketOrThrowException(username);
        bucket.getProducts().clear();
        log.info("All products cleared in bucket with username {}", username);
    }

    @Transactional
    @Override
    public void removeAllProductsOfThisType(String username, long productId) {
        var bucket = bucketInnerService.getBucketOrThrowException(username);
        bucket.getProducts().remove(productId);
        log.info("All products of type removed from bucket with username {}", username);
    }

    @Transactional
    @Override
    public void removeProduct(String username, long productId) {
        var bucket = bucketInnerService.getBucketOrThrowException(username);
        bucket.getProducts().computeIfPresent(productId,
                (k, quantity) -> quantity > 1 ? quantity - 1 : null);
        log.info("Product with id: {} removed from bucket with username {}", productId, username);
    }

    @Transactional
    @Override
    public void save(BucketEntity bucket) {
        bucketRepository.save(bucket);
    }

    @Transactional
    @Override
    public void addProducts(String username, long productId) {
        var bucket = bucketInnerService.getBucketOrThrowException(username);
        bucket.getProducts().merge(productId, 1, Integer::sum);
        log.info("Added products to bucket with username {}", username);
    }
}
