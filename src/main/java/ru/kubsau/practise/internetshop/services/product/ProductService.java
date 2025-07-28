package ru.kubsau.practise.internetshop.services.product;

import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.entities.Product;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductResponseDTO> getAll(int page, int size);

    Map<Long, Product> getProductsByIds(Collection<Long> productIds);
}
