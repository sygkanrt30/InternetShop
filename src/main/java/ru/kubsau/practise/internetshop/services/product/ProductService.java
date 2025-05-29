package ru.kubsau.practise.internetshop.services.product;

import ru.kubsau.practise.internetshop.entities.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getById(long id);
}
