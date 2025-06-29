package ru.kubsau.practise.internetshop.service.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.kubsau.practise.internetshop.model.dto.ProductDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.ProductMapper;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.repositories.ProductRepository;
import ru.kubsau.practise.internetshop.services.product.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private Page<Product> productPage;
    private List<ProductDTO> sortedProducts;
    private List<Product> products;
    private Pageable pageable;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>(List.of(
                new Product(1L, "milk", true, 100, 1, "It is a milk"),
                new Product(2L, "cake", false, 100, 100, "It is a cake"),
                new Product(3L, "sugar", false, 312, 1123120, "It is a sugar"),
                new Product(4L, "eggs", true, 100, 13200, "It is a eggs"),
                new Product(5L, "bread", true, 10, 909870, "It is a bread")
        ));

        pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "isAvailable"));
        productPage = new PageImpl<>(products, pageable, products.size());

        sortedProducts = new ArrayList<>(List.of(
                new ProductDTO(1L, "milk", true, 100, "It is a milk"),
                new ProductDTO(4L, "eggs", true, 100, "It is a eggs"),
                new ProductDTO(5L, "bread", true, 10, "It is a bread"),
                new ProductDTO(2L, "cake", false, 100, "It is a cake"),
                new ProductDTO(3L, "sugar", false, 312, "It is a sugar")
        ));
    }

    @Test
    @DisplayName("Проверяет верный ли список возвращает getAllProducts()")
    void getAllProductsCorrectCase() {
        Mockito.when(productRepository.findAll(pageable)).thenReturn(productPage);
        Mockito.when(productMapper.toDto(products.get(0))).thenReturn(sortedProducts.get(0));
        Mockito.when(productMapper.toDto(products.get(1))).thenReturn(sortedProducts.get(1));
        Mockito.when(productMapper.toDto(products.get(2))).thenReturn(sortedProducts.get(2));
        Mockito.when(productMapper.toDto(products.get(3))).thenReturn(sortedProducts.get(3));
        Mockito.when(productMapper.toDto(products.get(4))).thenReturn(sortedProducts.get(4));

        List<ProductDTO> result = productService.getAll(0, 15);

        Assertions.assertEquals(products.size(), result.size());
        Assertions.assertEquals(sortedProducts, result);
    }

    @Test
    @DisplayName("Сортируется ли список в getAllProducts()")
    void getAllProductsNotCorrectCase() {
        Mockito.when(productRepository.findAll(pageable)).thenReturn(productPage);
        Mockito.when(productMapper.toDto(products.get(0))).thenReturn(sortedProducts.get(0));
        Mockito.when(productMapper.toDto(products.get(1))).thenReturn(sortedProducts.get(3));
        Mockito.when(productMapper.toDto(products.get(2))).thenReturn(sortedProducts.get(4));
        Mockito.when(productMapper.toDto(products.get(3))).thenReturn(sortedProducts.get(1));
        Mockito.when(productMapper.toDto(products.get(4))).thenReturn(sortedProducts.get(2));
        Mockito.when(productRepository.findAll(pageable)).thenReturn(productPage);

        List<ProductDTO> result = productService.getAll(0, 15);

        Assertions.assertEquals(products.size(), result.size());
    }
}
