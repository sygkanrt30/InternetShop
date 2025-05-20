package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;

import java.util.Map;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/buckets")
public class BucketController {
    BucketService bucketService;

    @GetMapping(path = "/get-by-username/{username}")
    public Map<Product, Long> getByUsername(@PathVariable String username) {
        return bucketService.getProductsInBucket(username);
    }

    @PatchMapping("/clear-bucket/{username}")
    public ResponseEntity<String> clearBucket(@PathVariable String username) {
        bucketService.clearAllProductsInBucket(username);
        return getOkStatus();
    }

    @PatchMapping("/remove-all-products-this-type")
    public ResponseEntity<String> removeAllProductsOfThisTypeFromBucket(@RequestParam String username, @RequestParam long productId) {
        bucketService.removeAllProductsOfThisTypeFromBucket(username, productId);
        return getOkStatus();
    }

    @PatchMapping("/remove-product")
    public ResponseEntity<String> removeProductFromBucket(@RequestParam String username, @RequestParam long productId) {
        bucketService.removeProductFromBucket(username, productId);
        return getOkStatus();
    }

    @PatchMapping("/add-products")
    public ResponseEntity<String> addProductsToBucket(@RequestParam String username, @RequestParam long productId) {
        bucketService.addProductsToList(username, productId);
        return getOkStatus();
    }

    private ResponseEntity<String> getOkStatus() {
        return ResponseEntity.ok()
                .body("{\"message\":\"" + HttpStatus.OK.getReasonPhrase() + "\"}");
    }
}
