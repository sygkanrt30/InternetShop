package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsau.practise.internetshop.model.dto.ProductResponseDTO;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {
    ProductService productService;

    @GetMapping("/products")
    @PreAuthorize("isAuthenticated()")
    public List<ProductResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        return productService.getAll(page, size);
    }
}
