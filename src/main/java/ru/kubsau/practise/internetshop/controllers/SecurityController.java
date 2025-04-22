package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.entities.User;
import ru.kubsau.practise.internetshop.services.user.UserService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SecurityController {
    UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        userService.save(user);
        return "success";
    }
}
