package ru.kubsau.practise.internetshop.services.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.model.dto.ProductDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.ProductMapper;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override
    public List<ProductDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "isAvailable"));
        return productRepository.findAll(pageable)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public List<Product> getAllProductsAccordingToIds(List<Long> productIds) {
        List<Product> uniqueProd =  productRepository.findAllById(productIds);
        return productIds.stream()
                .map(id -> getByProductId(id, uniqueProd))
                .toList();
    }

    private Product getByProductId(Long id, List<Product> products) {
        return products.stream()
                .filter(prod -> prod.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
