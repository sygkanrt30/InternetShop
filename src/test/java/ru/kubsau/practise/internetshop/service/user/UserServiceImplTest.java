package ru.kubsau.practise.internetshop.service.user;

import com.sun.jdi.request.InvalidRequestStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kubsau.practise.internetshop.config.SecurityConfig;
import ru.kubsau.practise.internetshop.entities.Bucket;
import ru.kubsau.practise.internetshop.entities.User;
import ru.kubsau.practise.internetshop.repositories.UserRepository;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;
import ru.kubsau.practise.internetshop.services.user.UserServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BucketService bucketService;
    UserServiceImpl userServiceImpl;
    User userForIsRegistered;
    User userForSave;

    @BeforeEach
    public void setUp() {
        PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();
        userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder, bucketService);
        userForIsRegistered = new User("username", "email", passwordEncoder.encode("test300330"), null, new Bucket());
        userForSave = new User("slava", "slava.vy.2006@gmail.com", "Ss@159357", null, new Bucket());
    }

    @Test
    @DisplayName("Тест метода isRegistered корректный сценарий")
    public void isRegistered_CorrectCase() {
        String password = "test300330";
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(userForIsRegistered));

        boolean result = userServiceImpl.isRegistered("username", password);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Тест метода isRegistered некорректное имя")
    public void isRegistered_IncorrectName() {
        String password = "test303030";
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.isRegistered("username", password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test310330", "test300330 ", "twst300330"})
    @DisplayName("Тест метода isRegistered корректный пароль")
    public void isRegistered_IncorrectPassword(String password) {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(userForIsRegistered));

        boolean result = userServiceImpl.isRegistered("username", password);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Тест save(user) корректный сценарий")
    public void save_CorrectCase() {
        Assertions.assertDoesNotThrow(() -> userServiceImpl.save(userForSave));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username;№;!!", "w", "slava_dwd434#"})
    @DisplayName("Тест save(user) выброс ошибки некорректном имени")
    public void save_IncorrectUsername(String username) {
        userForSave.setUsername(username);

        Assertions.assertThrows(InvalidRequestStateException.class, () -> userServiceImpl.save(userForSave));
    }

    @ParameterizedTest
    @ValueSource(strings = {"slaa.23.@@mail.ru", "slra.vy_2566@gmail.com", "s2\\\"@gmail.com"})
    @DisplayName("Тест save(user) выброс ошибки некорректном email")
    public void save_IncorrectEmail(String email) {
        userForSave.setEmail(email);

        Assertions.assertThrows(InvalidRequestStateException.class, () -> userServiceImpl.save(userForSave));
    }

    @ParameterizedTest
    @ValueSource(strings = {"159357", "ssser@fes", "Ss124dwd2"})
    @DisplayName("Тест save(user) выброс ошибки некорректном пароля")
    public void save_IncorrectPassword(String password) {
        userForSave.setPassword(password);

        Assertions.assertThrows(InvalidRequestStateException.class, () -> userServiceImpl.save(userForSave));
    }

    @Test
    @DisplayName("Тест save(user) ошибка в методе взаимодействия с бд")
    public void save_IncorrectCase() {
        Mockito.when(userRepository.save(Mockito.any())).thenThrow(InvalidRequestStateException.class);

        Assertions.assertThrows(InvalidRequestStateException.class, () -> userServiceImpl.save(userForSave));
    }
}
