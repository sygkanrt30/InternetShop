package ru.kubsau.practise.internetshop.services.order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;

@Component
@AllArgsConstructor
public class ProductAvailabilityTracker {
    ProductRepository productRepository;

    @Transactional
    public void decrementCount(ProductResponseDTO product, long amountDecrement) {
        throwIfInsufficientProductCount(product, amountDecrement);
        var id = product.id();
        var count = product.count();
        if (count == amountDecrement) {
            productRepository.makeAvailableFalse(id);
        }
        productRepository.updateCount(id, count - amountDecrement);
    }

    private void throwIfInsufficientProductCount(ProductResponseDTO product, long amountDecrement) {
        if (product.count() < amountDecrement)
            throw new IllegalStateException("There is no so much of this product in the warehouse");
    }
}
