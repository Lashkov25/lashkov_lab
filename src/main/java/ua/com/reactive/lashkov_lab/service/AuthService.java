package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.AuthResponse;
import ua.com.reactive.lashkov_lab.dto.LoginDto;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.entity.Role;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.exception.BusinessException;
import ua.com.reactive.lashkov_lab.exception.ErrorMessages;
import ua.com.reactive.lashkov_lab.security.JwtUtil;
import ua.com.reactive.lashkov_lab.repository.UserRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Mono<AuthResponse> registerUser(UserRegistrationDto registrationDto, boolean isAdmin) {
        return userRepository.findByUsername(registrationDto.getUsername())
                .flatMap(existingUser -> Mono.<AuthResponse>error(new BusinessException(ErrorMessages.USERNAME_EXISTS)))
                .switchIfEmpty(createUser(registrationDto, isAdmin))
                .cast(AuthResponse.class);
    }

    private Mono<AuthResponse> createUser(UserRegistrationDto registrationDto, boolean isAdmin) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setRole(isAdmin ? Role.ADMIN : Role.USER);
        user.setBalance(BigDecimal.ZERO);
        
        return userRepository.save(user)
                .map(savedUser -> {
                    String token = jwtUtil.generateToken(savedUser);
                    return new AuthResponse(token, savedUser.getUsername(), savedUser.getRole());
                });
    }

    public Mono<AuthResponse> login(LoginDto loginDto) {
        return userRepository.findByUsername(loginDto.getUsername())
                .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    return new AuthResponse(token, user.getUsername(), user.getRole());
                })
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.INVALID_CREDENTIALS)));
    }
}
