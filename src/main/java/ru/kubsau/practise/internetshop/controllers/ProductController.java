package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.entities.Product;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }
}
