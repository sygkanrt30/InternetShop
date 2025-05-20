package ru.kubsau.practise.internetshop.services.order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;

@SuppressWarnings("ALl")
@Component
@AllArgsConstructor
public class ProductAvailabilityTracker {
    ProductRepository productRepository;

    @Transactional
    public void decrementProductCount(Product product, long amountDecrement) {
        if (isNotEnoughProductInDB(product, amountDecrement)) {
            throw new IllegalStateException("There is no so much of this product in the warehouse");
        }
        decrementCount(amountDecrement, product);
    }

    private void decrementCount(long amountDecrement, Product product) {
        var id = product.getId();
        if (isChangeFieldOfAvailability(id, amountDecrement)) {
            productRepository.makeFalseIsAvailableColumn(id);
        }
        productRepository.decrementProductCount(id, amountDecrement);
    }

    private boolean isNotEnoughProductInDB(Product product, long amountDecrement) {
        return product.getCount() < amountDecrement;
    }

    private boolean isChangeFieldOfAvailability(long id, long amountDecrement) {
        return productRepository.getById(id).getCount() == amountDecrement;
    }
}
