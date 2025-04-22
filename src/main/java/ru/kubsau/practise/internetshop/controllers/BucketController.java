package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.entities.Bucket;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;

import java.util.List;

@AllArgsConstructor
@RestController()
@RequestMapping("/buckets")
public class BucketController {
    BucketService bucketService;

    @GetMapping(path = "/get-by-username/{username}")
    public List<Product> getByUsername(@PathVariable String username) {
        return bucketService.getProductsInBucket(username.trim());
    }

    @PatchMapping("/clear-bucket/{username}")
    public void clearBucket(@PathVariable String username) {
        bucketService.clearAllProductsInBucket(username.trim());
    }

    @PatchMapping("/remove-product")
    public void removeProductFromBucket(@RequestParam String username, @RequestParam long productId) {
        bucketService.removeProductFromBucket(username.trim(), productId);
    }

    @PatchMapping("/add-products")
    public void addProductsFromBucket(@RequestBody Bucket bucket) {
        bucketService.addProductsToList(bucket);
    }
}
