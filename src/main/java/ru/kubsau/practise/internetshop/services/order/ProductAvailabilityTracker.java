package ru.kubsau.practise.internetshop.services.order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;
import ru.kubsau.practise.internetshop.services.product.ProductService;

@SuppressWarnings("ALl")
@Component
@AllArgsConstructor
public class ProductAvailabilityTracker {
    ProductService productService;
    ProductRepository productRepository;

    @Transactional
    public void decrementProductCount(Product product, long amountDecrement) {
        var id = product.getId();
        if (isNotEnoughProductInDB(product, amountDecrement)) {
            throw new IllegalStateException("There is no so much of this product in the warehouse");
        }
        decrementCount(amountDecrement, id);
    }

    private void decrementCount(long amountDecrement, long id) {
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
