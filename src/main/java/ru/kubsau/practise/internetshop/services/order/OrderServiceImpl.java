package ru.kubsau.practise.internetshop.services.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    BucketService bucketService;
    ProductAvailabilityTracker productAvailabilityTracker;
    ExcelFileCreator fileCreator;

    @Override
    @Transactional
    public void createOrder(String username) {
        var products = getProductsInBucketOrThrow(username);
        fileCreator.createExcelDocument(products, username);
        removeGarbageDataInOtherTables(username, products);
        log.info("Order created successfully");
    }

    private Map<ProductResponseDTO, Integer> getProductsInBucketOrThrow(String username) {
        Map<ProductResponseDTO, Integer> products = bucketService.getBucket(username);
        if (products.isEmpty()) {
            throw new IllegalStateException("There are no products in bucket");
        }
        return products;
    }

    private void removeGarbageDataInOtherTables(String username, Map<ProductResponseDTO, Integer> map) {
        updateProductsCount(map);
        bucketService.clearBucket(username);
    }

    private void updateProductsCount(Map<ProductResponseDTO, Integer> map) {
        map.forEach(productAvailabilityTracker::decrementCount);
    }
}
