package ru.kubsau.practise.internetshop.services.product;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::isAvailable).reversed())
                .toList();
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product with id `%s` not found".formatted(id)));
    }
}
