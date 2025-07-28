package ru.kubsau.practise.internetshop.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.kubsau.practise.internetshop.model.dto.UserRequestDTO;

@Component
@RequiredArgsConstructor
public class Authenticator {
    AuthenticationManager authenticationManager;

    public void doAuthentication(UserRequestDTO user, HttpServletRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        var authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }
}
