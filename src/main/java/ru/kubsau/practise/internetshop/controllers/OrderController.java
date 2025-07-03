package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.util.AuthenticationContext;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTO;
import ru.kubsau.practise.internetshop.services.order.OrderService;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTOCreator;

@RestController
@AllArgsConstructor
@RequestMapping("/order/")
public class OrderController {
    OrderService orderService;

    @PostMapping("create-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO createOrder() {
        String currentUsername = AuthenticationContext.getCurrentUsername();
        orderService.createOrder(currentUsername);
        return ResponseDTOCreator.getResponseOK();
    }
}
