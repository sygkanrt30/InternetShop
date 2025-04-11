package ru.kubsau.practise.internetshop.services.product;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.repositories.product.Product;
import ru.kubsau.practise.internetshop.repositories.product.ProductRepository;
import ru.kubsau.practise.internetshop.services.exceptions.InvalidRequestException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::isSuitable).reversed())
                .toList();
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product with id `%s` not found".formatted(id))
        );
    }

    @Override
    public void deleteById(long id) {
        val product = getProductById(id);
        productRepository.deleteById(id);
        log.info("Deleted product `{}`", product);
    }

    @Override
    public void deleteByName(String name) {
        Optional<Product> product = productRepository.deleteProductByName(name);
        logDeleteByNameAction(product);
    }

    private void logDeleteByNameAction(Optional<Product> product) {
        product.ifPresentOrElse(
                value -> log.info("Product with name {} deleted", value.getName()),
                () -> log.info("No product found to delete")
        );
    }

    @Override
    @Transactional
    public void update(long id, Product product) {
        tryToUpdateProduct(id, product);
        log.info("Updated product with id: `{}`", id);
    }

    @Override
    public void updatePrice(long id, long price) {
        getProductById(id);
        if (price < 1)
            productRepository.updatePrice(id, price);
    }

    private void tryToUpdateProduct(long id, Product product) {
        if (productRepository.existsById(id)) {
            try {
                trimStringFields(product);
                checkProductFields(product);
                productRepository.updateProduct(id,
                        product.getName().trim().toLowerCase(),
                        product.isSuitable(),
                        product.getCount(),
                        product.getPrice(),
                        product.getDescription());
            } catch (Exception e) {
                throw new InvalidRequestException(e.getMessage());
            }
        } else {
            save(product);
        }
    }

    private void trimStringFields(Product product) {
        product.setName(product.getName().trim().toLowerCase());
        product.setDescription(product.getDescription().trim());
    }

    private void checkProductFields(Product product) {
        if (product.getPrice() < 1 ||
                (product.isSuitable() && product.getCount() < 1)) {
            throw new RuntimeException("The count or price of the product is incorrect");
        }
    }

    @Override
    public void save(Product product) {
        tryToSaveProduct(product);
        log.info("Saved product `{}`", product);
    }

    private void tryToSaveProduct(Product product) {
        try {
            trimStringFields(product);
            checkProductFields(product);
            productRepository.save(product);
        } catch (Exception e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }
}
