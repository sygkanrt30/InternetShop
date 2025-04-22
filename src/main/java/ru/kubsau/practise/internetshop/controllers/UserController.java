package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.services.user.UserService;

@RestController
@RequestMapping(value = "/user")
@AllArgsConstructor
public class UserController {
    UserService userService;

    @PatchMapping("/update-username")
    public void updateUsername(@RequestParam String newUsername, @RequestParam String oldUsername) {
        userService.updateUsername(newUsername, oldUsername);
    }
}
