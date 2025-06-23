package ru.kubsau.practise.internetshop.security;

import lombok.AllArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kubsau.practise.internetshop.model.entities.User;

import java.util.Arrays;
import java.util.Collection;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @NonFinal User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();
        return Arrays.stream(role.split(", "))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
