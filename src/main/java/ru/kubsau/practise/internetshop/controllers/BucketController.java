package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTOCreator;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;
import ru.kubsau.practise.internetshop.util.AuthenticationContext;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/buckets/")
public class BucketController {
    BucketService bucketService;

    @GetMapping("get")
    @PreAuthorize("isAuthenticated()")
    public Map<ProductResponseDTO, Integer> get() {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        return bucketService.getBucket(currentUsername);
    }

    @PatchMapping("clear-bucket")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO clear() {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        bucketService.clearBucket(currentUsername);
        return ResponseDTOCreator.getResponseOK();
    }

    @PatchMapping("remove-all-products-this-type")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO removeAllProductsOfThisType(@RequestParam long productId) {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        bucketService.removeAllProductsOfThisType(currentUsername, productId);
        return ResponseDTOCreator.getResponseOK();
    }

    @PatchMapping("remove-product")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO removeProduct(@RequestParam long productId) {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        bucketService.removeProduct(currentUsername, productId);
        return ResponseDTOCreator.getResponseOK();
    }

    @PatchMapping("add-products")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO addProducts(@RequestParam long productId) {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        bucketService.addProducts(currentUsername, productId);
        return ResponseDTOCreator.getResponseOK();
    }
}
