package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.services.order.OrderService;


@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    OrderService orderService;

    @PostMapping(path = "/create-order/{username}")
    public ResponseEntity<String> createOrder(@PathVariable String username) {
        orderService.createOrder(username);
        return ResponseEntity.ok()
                .body("{\"message\":\"" + HttpStatus.OK.getReasonPhrase() + "\"}");
    }
}
