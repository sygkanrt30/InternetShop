package ru.kubsau.practise.internetshop.services.product;

import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.entities.Product;

import java.util.Collection;
import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAll(int page, int size);

    List<Product> getProductsAccordingToIds(Collection<Long> productIds);
}
