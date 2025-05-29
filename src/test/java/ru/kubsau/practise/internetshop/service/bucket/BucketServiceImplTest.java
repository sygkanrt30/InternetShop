package ru.kubsau.practise.internetshop.service.bucket;

import com.sun.jdi.request.InvalidRequestStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kubsau.practise.internetshop.entities.Bucket;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.repositories.BucketRepository;
import ru.kubsau.practise.internetshop.services.bucket.BucketServiceImpl;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BucketServiceImplTest {
    @Mock
    BucketRepository bucketRepository;
    @Mock
    ProductService productService;
    @InjectMocks
    BucketServiceImpl bucketServiceImpl;

    @Test
    @DisplayName("1 getProductsInBucket(username) на формирование мапы")
    public void getProductsInBucket1() {
        var product1 = new Product(2L, "milk", true, 100, 1, "It is a milk");
        var product2 = new Product(3L, "cake", false, 100, 1, "It is a cake");
        var product3 = new Product(4L, "eggs", true, 100, 1, "It is a eggs");
        var productsInBucket = new HashMap<>(Map.of(
                product1, 3L,
                product2, 1L,
                product3, 3L
        ));
        Mockito.when(bucketRepository.getIdsInString(Mockito.anyString())).thenReturn("2,2,2,4,3,4,4");
        Mockito.when(productService.getById(2L)).thenReturn(product1);
        Mockito.when(productService.getById(3L)).thenReturn(product2);
        Mockito.when(productService.getById(4L)).thenReturn(product3);

        Map<Product, Long> result = bucketServiceImpl.getProductsInBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getProductsInBucket("username"));
        Assertions.assertEquals(productsInBucket.get(product1), result.get(product1));
        Assertions.assertEquals(productsInBucket.size(), result.size());
        Assertions.assertEquals(productsInBucket, result);
        Assertions.assertTrue(result.containsKey(product2));
    }

    @Test
    @DisplayName("2 getProductsInBucket(username) на формирование мапы")
    public void getProductsInBucket2() {
        var product1 = new Product(2L, "milk", true, 100, 1, "It is a milk");
        var productsInBucket = new HashMap<>(Map.of(
                product1, 1L
        ));
        Mockito.when(bucketRepository.getIdsInString(Mockito.anyString())).thenReturn("2");
        Mockito.when(productService.getById(2L)).thenReturn(product1);

        Map<Product, Long> result = bucketServiceImpl.getProductsInBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getProductsInBucket("username"));
        Assertions.assertEquals(productsInBucket.get(product1), result.get(product1));
        Assertions.assertEquals(productsInBucket.size(), result.size());
        Assertions.assertEquals(productsInBucket, result);
    }

    @Test
    @DisplayName("3 getProductsInBucket(username) на формирование мапы")
    public void getProductsInBucket3() {
        var productsInBucket = new HashMap<>();
        Mockito.when(bucketRepository.getIdsInString(Mockito.anyString())).thenReturn("");

        Map<Product, Long> result = bucketServiceImpl.getProductsInBucket("username");

        Assertions.assertDoesNotThrow(() -> bucketServiceImpl.getProductsInBucket("username"));
        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(productsInBucket, result);
    }

    @Test
    @DisplayName("getProductsInBucket(username) выброс ошибки из-за некорректного id")
    public void getProductsInBucket_InvalidId() {
        Mockito.when(bucketRepository.getIdsInString(Mockito.anyString())).thenReturn("w,1,2,3");

        Assertions.assertThrows(NumberFormatException.class, () -> bucketServiceImpl.getProductsInBucket("username"));
    }

    @Test
    @DisplayName("clearBucket(String username) корректный сценарий")
    public void clearBucket_CorrectCase() {
        long[] emptyArray = new long[0];
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateListOfProductsInBucket(Mockito.anyString(), Mockito.eq(emptyArray));

        var exception = Assertions.assertThrows(RuntimeException.class,
                () -> bucketRepository.updateListOfProductsInBucket("someString", emptyArray));

        Assertions.assertEquals("Forced exception for testing", exception.getMessage());
    }

    @Test
    @DisplayName("clearBucket(String username) выброс ошибки из-за некорректного username")
    public void clearBucket_InvalidUsername() {
        Mockito.when(bucketRepository.getBucket(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidRequestStateException.class, () -> bucketServiceImpl.clearBucket("username"));
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) корректный сценарий")
    public void removeAllProductsOfThisType_CorrectCase() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        long[] filteredArr = {1L, 2L, 2L, 2L};
        Mockito.when(bucketRepository.getBucket(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateListOfProductsInBucket(Mockito.anyString(), Mockito.eq(filteredArr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) корректный сценарий 2")
    public void removeAllProductsOfThisType_CorrectCase2() {
        long[] arr = {2L, 2L, 2L, 2L, 2L, 2L};
        long[] emptyArray = new long[0];
        Mockito.when(bucketRepository.getBucket(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateListOfProductsInBucket(Mockito.anyString(), Mockito.eq(emptyArray));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) указаного id нет в списке")
    public void removeAllProductsOfThisType_IdNotInBucket() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        Mockito.when(bucketRepository.getBucket(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateListOfProductsInBucket(Mockito.anyString(), Mockito.eq(arr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 7L));
    }

    @Test
    @DisplayName("removeAllProductsOfThisType(username, productId) нет корзины с таким владельцем")
    public void removeAllProductsOfThisType_InvalidUsername() {
        Mockito.when(bucketRepository.getBucket("username"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeProduct(username, productId) корректный сценарий")
    public void removeProduct_CorrectCase() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        long[] filteredArr = {1L, 2L, 3L, 2L, 2L};
        Mockito.when(bucketRepository.getBucket(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateListOfProductsInBucket(Mockito.anyString(), Mockito.eq(filteredArr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeProduct(username, productId) корректный сценарий 2")
    public void removeProduct_CorrectCase2() {
        long[] arr = {2L};
        long[] emptyArray = new long[0];
        Mockito.when(bucketRepository.getBucket(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateListOfProductsInBucket(Mockito.anyString(), Mockito.eq(emptyArray));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("removeProduct(username, productId) указаного id нет в списке")
    public void removeProduct_IdNotInBucket() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        Mockito.when(bucketRepository.getBucket(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateListOfProductsInBucket(Mockito.anyString(), Mockito.eq(arr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 7L));
    }

    @Test
    @DisplayName("removeProduct(username, productId) нет корзины с таким владельцем")
    public void removeProduct_InvalidUsername() {
        Mockito.when(bucketRepository.getBucket("username"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("addProductsToBucket(username, productId) корректный сценарий")
    public void addProductsToBucket_CorrectCase() {
        long[] arr = {1L, 2L, 3L, 3L, 2L, 2L};
        long[] newArr = {1L, 2L, 3L, 3L, 2L, 2L, 3L};
        Mockito.when(bucketRepository.getBucket(Mockito.anyString()))
                .thenReturn(Optional.of(new Bucket("username", arr)));
        Mockito.doThrow(new RuntimeException("Forced exception for testing"))
                .when(bucketRepository).updateListOfProductsInBucket(Mockito.anyString(), Mockito.eq(newArr));

        Assertions.assertThrows(RuntimeException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }

    @Test
    @DisplayName("addProductsToBucket(username, productId) нет корзины с таким владельцем")
    public void addProductsToBucket_InvalidUsername() {
        Mockito.when(bucketRepository.getBucket("username"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidRequestStateException.class,
                () -> bucketServiceImpl.removeAllProductsOfThisType("username", 3L));
    }
}
