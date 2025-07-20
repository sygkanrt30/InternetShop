package ru.kubsau.practise.internetshop.services.user;


import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kubsau.practise.internetshop.security.UserDetailsImpl;
import ru.kubsau.practise.internetshop.model.entities.User;
import ru.kubsau.practise.internetshop.repositories.UserRepository;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;
import ru.kubsau.practise.internetshop.services.user.enums.Role;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    BucketService bucketService;

    @Override
    @Transactional
    public void save(User user) {
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            bucketService.save(user.getBucket());
            userRepository.save(user);
        } catch (Exception e) {
            throw new InvalidRequestStateException(e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
