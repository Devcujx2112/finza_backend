package com.finza.backend.config;


import com.finza.backend.constant.BaseMessage;
import com.finza.backend.repository.Account_repository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final Account_repository accountRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> accountRepository.findByEmail(username)
                .map(account -> org.springframework.security.core.userdetails.User
                        .withUsername(account.getEmail())
                        .password(account.getPassword())
                        .roles(account.getRole().name())
                        .build())
                .orElseThrow(() -> new RuntimeException(BaseMessage.ACCOUNT_NOT_FOUND));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Không cần token
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/refresh-token").permitAll()
                        .requestMatchers("/auth/login-social").permitAll()
                        .requestMatchers("/auth/trial").permitAll()
                        .requestMatchers("/auth/send-otp").permitAll()
                        .requestMatchers("/auth/verify-otp").permitAll()
                        .requestMatchers("auth/change-password").permitAll()
                        // Còn lại phải có token
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder facebookJwtDecoder() {
        return NimbusJwtDecoder
                .withJwkSetUri("https://www.facebook.com/.well-known/oauth/openid/jwks/")
                .build();
    }
}
