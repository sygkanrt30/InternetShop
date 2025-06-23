package ru.kubsau.practise.internetshop.services.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsau.practise.internetshop.model.entities.Product;
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
        Map<Product, Long> products = getProductsInBucketOrThrow(username);
        fileCreator.createXlsxFile(products, username);
        removeGarbageDataInOtherTables(username, products);
        log.info("Order created successfully");
    }

    private Map<Product, Long> getProductsInBucketOrThrow(String username) {
        Map<Product, Long> products = bucketService.getBucket(username);
        if (products.isEmpty()) {
            throw new IllegalStateException("There are no products in bucket");
        }
        return products;
    }

    private void removeGarbageDataInOtherTables(String username, Map<Product, Long> map) {
        updateProductsCount(map);
        bucketService.clearBucket(username);
    }

    private void updateProductsCount(Map<Product, Long> map) {
        map.forEach(productAvailabilityTracker::decrementCount);
    }
}
