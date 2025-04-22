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

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @Override
    @Transactional
    public void updateUsername(String newUsername, String oldUsername) {
        userRepository.findByUsername(oldUsername)
                .orElseThrow(() -> new UsernameNotFoundException(oldUsername + " not found"));
        tryToUpdateUsername(newUsername, oldUsername);
    }

    private void tryToUpdateUsername(String newUsername, String oldUsername) {
        try {
            bucketService.updateUsername(newUsername, oldUsername);
            userRepository.updateUsername(newUsername, oldUsername);
        } catch (Exception e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }
}
