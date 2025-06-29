package ru.kubsau.practise.internetshop.security.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.kubsau.practise.internetshop.repositories.UserRepository;
import ru.kubsau.practise.internetshop.services.bucket.BucketService;
import ru.kubsau.practise.internetshop.services.user.UserServiceImpl;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    UserRepository userRepository;
    BucketService bucketService;
    Environment env;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl(userRepository, passwordEncoder(), bucketService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        String frontUrl = Objects.requireNonNull(env.getProperty("spring.frontend.base.url"));
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        String registerPath = Objects.requireNonNull(env.getProperty("spring.path.register"));
        String loginPath = Objects.requireNonNull(env.getProperty("spring.path.login"));
        String defaultRedirectPath = Objects.requireNonNull(env.getProperty("spring.path.default-redirect"));
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(registerPath).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.loginPage(loginPath).permitAll()
                        .defaultSuccessUrl(defaultRedirectPath))
                .authenticationManager(authenticationManager())
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository())
                        .configure(http)
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        var provider = authenticationProvider();
        return new ProviderManager(provider);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
