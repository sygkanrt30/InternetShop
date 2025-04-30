package ru.kubsau.practise.internetshop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kubsau.practise.internetshop.entities.User;
import ru.kubsau.practise.internetshop.services.user.UserService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SecurityController {
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        userService.save(user);
        return getOkStatus();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        if (userService.isRegistered(user.getUsername(), user.getPassword())) {
            return getOkStatus();
        }
        return getErrorStatus();
    }

    private ResponseEntity<String> getOkStatus() {
        return ResponseEntity.ok()
                .body("{\"message\":\"" + HttpStatus.OK.getReasonPhrase() + "\"}");
    }

    private ResponseEntity<String> getErrorStatus() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body("{\"message\":\"" + HttpStatus.UNAUTHORIZED.getReasonPhrase() + "\"}");
    }
}
