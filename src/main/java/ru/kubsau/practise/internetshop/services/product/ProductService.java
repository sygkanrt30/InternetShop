package ru.kubsau.practise.internetshop.services.product;

import ru.kubsau.practise.internetshop.repositories.product.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(long id);

    void deleteById(long id);

    void deleteByName(String name);

    void save(Product product);

    void update(long id, Product product);

    void updatePrice(long id, long price);
}
