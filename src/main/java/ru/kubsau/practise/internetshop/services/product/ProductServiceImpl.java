package ru.kubsau.practise.internetshop.services.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    @Override
    public List<ProductResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "isAvailable"));
        return productRepository.findAllBy(ProductResponseDTO.class, pageable)
                .stream()
                .toList();
    }

    @Override
    public List<Product> getProductsAccordingToIds(Collection<Long> productIds) {
        return productRepository.findAllById(productIds.stream().toList());
    }
}
