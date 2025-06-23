package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsau.practise.internetshop.model.dto.ProductDTO;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.ProductMapper;
import ru.kubsau.practise.internetshop.model.entities.Product;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;
import ru.kubsau.practise.internetshop.util.AuthenticationContext;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTOCreator;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
public class BucketController {
    BucketService bucketService;
    ProductMapper productMapper;

    @GetMapping("/buckets/get")
    @PreAuthorize("isAuthenticated()")
    public Map<ProductDTO, Long> get() {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        Map<Product, Long> map = bucketService.getBucket(currentUsername);
        var mapWithDto = new HashMap<ProductDTO, Long>();
        map.forEach((prod, countInBucket)
                -> mapWithDto.put(productMapper.toDto(prod), countInBucket));
        return mapWithDto;
    }

    @PatchMapping("/buckets/clear-bucket")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO clear() {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        bucketService.clearBucket(currentUsername);
        return ResponseDTOCreator.getResponseOK();
    }

    @PatchMapping("/buckets/remove-all-products-this-type")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO removeAllProductsOfThisType(@RequestParam long productId) {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        bucketService.removeAllProductsOfThisType(currentUsername, productId);
        return ResponseDTOCreator.getResponseOK();
    }

    @PatchMapping("/buckets/remove-product")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO removeProduct(@RequestParam long productId) {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        bucketService.removeProduct(currentUsername, productId);
        return ResponseDTOCreator.getResponseOK();
    }

    @PatchMapping("/buckets/add-products")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO addProducts(@RequestParam long productId) {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        bucketService.addProducts(currentUsername, productId);
        return ResponseDTOCreator.getResponseOK();
    }
}
