package ru.kubsau.practise.internetshop.services.order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;

@Component
@AllArgsConstructor
public class ProductAvailabilityTracker {
    ProductRepository productRepository;

    @Transactional
    public void decrementProductCount(Product product, long amountDecrement) {
        if (product.getCount() < amountDecrement) {
            throw new IllegalStateException("There is no so much of this product in the warehouse");
        }
        decrementCount(product, amountDecrement);
    }

    private void decrementCount(Product product, long amountDecrement) {
        var id = product.getId();
        if (product.getCount() == amountDecrement) {
            productRepository.makeFalseIsAvailableColumn(id);
        }
        productRepository.decrementProductCount(id, amountDecrement);
    }
}
