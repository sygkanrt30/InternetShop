package ru.kubsau.practise.internetshop.services.user;


import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.config.UserDetailsImpl;
import ru.kubsau.practise.internetshop.entities.User;
import ru.kubsau.practise.internetshop.repositories.UserRepository;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    BucketService bucketService;

    @Override
    @Transactional
    public void save(User user) {
        UserValidator.validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        tryToSaveUser(user);
    }

    private void tryToSaveUser(User user) {
        try {
            bucketService.create(user.getUsername());
            userRepository.save(user);
        } catch (Exception e) {
            throw new InvalidRequestStateException(e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = getUserOrElseThrow(username);
        return new UserDetailsImpl(user);
    }

    private User getUserOrElseThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    @Override
    public boolean isRegistered(String username, String password) {
        User userForUsername = getUserOrElseThrow(username);
        return passwordEncoder.matches(password, userForUsername.getPassword());
    }
}
