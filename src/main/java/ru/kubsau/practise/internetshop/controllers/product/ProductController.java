package ru.kubsau.practise.internetshop.controllers.product;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.repositories.product.Product;
import ru.kubsau.practise.internetshop.services.product.ProductService;

import java.util.List;

@RestController
@RequestMapping("/rest_api/products")
@AllArgsConstructor
public class ProductController {
    ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable long id) {
        return productService.getProductById(id);
    }
    //admin endpoint
    @PostMapping("/create")
    public void create(@RequestBody Product product) {
        productService.save(product);
    }
    //admin endpoint
    @DeleteMapping(path = "/delete/{id}")
    public void deleteById(@PathVariable long id) {
        productService.deleteById(id);
    }
    //admin endpoint
    @DeleteMapping(path = "/delete_by_name/{name}")
    public void deleteByName(@PathVariable String name) {
        productService.deleteByName(name.trim().toLowerCase());
    }
    //admin endpoint
    @PutMapping(path = "update/{id}")
    public void update(@PathVariable long id, @RequestBody Product product) {
        productService.update(id, product);
    }
    //admin endpoint
    @PatchMapping(path = "update_price/{id}")
    public void updatePrice(@PathVariable long id, long price) {
        productService.updatePrice(id, price);
    }
}
