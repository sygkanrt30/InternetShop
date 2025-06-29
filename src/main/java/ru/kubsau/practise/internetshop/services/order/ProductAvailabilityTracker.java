package ru.kubsau.practise.internetshop.services.order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;

@Component
@AllArgsConstructor
public class ProductAvailabilityTracker {
    ProductRepository productRepository;

    @Transactional
    public void decrementCount(Product product, long amountDecrement) {
        throwIfInsufficientProductCount(product, amountDecrement);
        var id = product.getId();
        var count = product.getCount();
        if (count == amountDecrement) {
            productRepository.makeAvailableFalse(id);
        }
        productRepository.updateCount(id, count - amountDecrement);
    }

    private void throwIfInsufficientProductCount(Product product, long amountDecrement) {
        if (product.getCount() < amountDecrement)
            throw new IllegalStateException("There is no so much of this product in the warehouse");
    }
}
