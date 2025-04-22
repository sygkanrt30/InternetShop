package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.services.order.OrderService;


@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    OrderService orderService;

    @PostMapping(path = "/create-order/{username}")
    public String createOrder(@PathVariable String username) {
        return orderService.createOrder(username);
    }
}
