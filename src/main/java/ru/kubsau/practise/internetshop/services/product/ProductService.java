package ru.kubsau.practise.internetshop.services.product;

import ru.kubsau.practise.internetshop.model.dto.ProductDTO;
import ru.kubsau.practise.internetshop.model.entities.Product;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll(int page, int size);

    List<Product> getAllProductsAccordingToIds(List<Long> productIds);
}
