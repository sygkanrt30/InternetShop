package ru.kubsau.practise.internetshop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTO;
import ru.kubsau.practise.internetshop.model.dto.ResponseDTOCreator;
import ru.kubsau.practise.internetshop.model.dto.UserDTO;
import ru.kubsau.practise.internetshop.model.dto.mapper.UserMapper;
import ru.kubsau.practise.internetshop.services.user.UserService;

@RestController
@AllArgsConstructor
public class SecurityController {
    UserService userService;
    UserMapper userMapper;
    AuthenticationManager authenticationManager;

    @PostMapping("/auth/register")
    public ResponseDTO register(@RequestBody UserDTO user, HttpServletRequest request) {
        userService.save(userMapper.fromDto(user));
        doAuthentication(user, request);
        return ResponseDTOCreator.getResponseOK();
    }

    private void doAuthentication(UserDTO user, HttpServletRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        var authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }
}
