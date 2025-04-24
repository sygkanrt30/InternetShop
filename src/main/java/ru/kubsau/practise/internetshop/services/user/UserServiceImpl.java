package ru.kubsau.practise.internetshop.services.user;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.config.UserDetailsImpl;
import ru.kubsau.practise.internetshop.entities.User;
import ru.kubsau.practise.internetshop.repositories.UserRepository;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;
import ru.kubsau.practise.internetshop.services.exceptions.InvalidRequestException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    BucketService bucketService;
    UserValidator userValidator;

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userValidator.validate(user);
        tryToSaveUser(user);
    }

    private void tryToSaveUser(User user) {
        try {
            bucketService.createBucket(user.getUsername());
            userRepository.save(user);
        } catch (Exception e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
        return new UserDetailsImpl(user);
    }
}
