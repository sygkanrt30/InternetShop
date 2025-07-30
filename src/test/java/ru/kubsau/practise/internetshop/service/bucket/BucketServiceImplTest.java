package ru.kubsau.practise.internetshop.service.bucket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.ProductMapper;
import ru.kubsau.practise.internetshop.model.entities.BucketEntity;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.services.bucket.BucketEntityService;
import ru.kubsau.practise.internetshop.services.bucket.BucketServiceImpl;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class BucketServiceImplTest {
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductService productService;
    @Mock
    private BucketEntityService bucketInnerService;
    @InjectMocks
    private BucketServiceImpl bucketServiceImpl;

    @Test
    @DisplayName("1 getProductsInBucket(username) на формирование мапы")
    public void getProductsInBucket1() {
        var productDto = new ProductResponseDTO(2L, "milk", true, 100, 1, "It is a milk");
        var product = new Product(2L, "milk", true, 100, 1, "It is a milk");
        var productsInBucket = new HashMap<>(Map.of(
                2L, 1
        ));

        var bucket = new BucketEntity("username", productsInBucket);

        Mockito.when(productService.getProductsByIds(Set.of(2L))).thenReturn(Map.of(2L, product));
        Mockito.when(bucketInnerService.getBucketOrThrowException(Mockito.anyString())).thenReturn(bucket);
        Mockito.when(productMapper.toDto(product)).thenReturn(productDto);

        Map<ProductResponseDTO, Integer> result = bucketServiceImpl.getBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getBucket("username"));
        Assertions.assertEquals(productsInBucket.get(productDto.id()), result.get(productDto));
        Assertions.assertEquals(productsInBucket.size(), result.size());
    }

    @Test
    @DisplayName("2 getProductsInBucket(username) пустая мапа")
    public void getProductsInBucket2() {
        var emptyMap = new HashMap<Long, Integer>();
        var bucket = new BucketEntity("username", emptyMap);
        Mockito.when(bucketInnerService.getBucketOrThrowException(Mockito.anyString())).thenReturn(bucket);
        Mockito.when(productService.getProductsByIds(Set.of())).thenReturn(Map.of());

        Map<ProductResponseDTO, Integer> result = bucketServiceImpl.getBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getBucket("username"));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("2 clearBucket(String username) корректный сценарий")
    public void clearBucket2_CorrectCase() {
        var emptyMap = new HashMap<Long, Integer>();
        BucketEntity bucket = new BucketEntity("username", emptyMap);

        Mockito.when(bucketInnerService.getBucketOrThrowException(Mockito.anyString())).thenReturn(bucket);

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.clearBucket("username"));
    }
}
