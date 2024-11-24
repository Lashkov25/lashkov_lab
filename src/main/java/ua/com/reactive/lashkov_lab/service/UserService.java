package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.entity.Role;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.exception.BusinessException;
import ua.com.reactive.lashkov_lab.exception.ErrorMessages;
import ua.com.reactive.lashkov_lab.repository.UserRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.USER_NOT_FOUND)));
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.USER_NOT_FOUND)));
    }

    public Mono<User> registerUser(UserRegistrationDto registrationDto) {
        return userRepository.findByUsername(registrationDto.getUsername())
                .flatMap(existingUser -> Mono.<User>error(new BusinessException(ErrorMessages.USERNAME_EXISTS)))
                .switchIfEmpty(createUser(registrationDto))
                .cast(User.class);
    }

    private Mono<User> createUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setRole(Role.USER);
        user.setBalance(BigDecimal.ZERO);
        return userRepository.save(user);
    }

    public Mono<User> updateUserBalance(Long userId, BigDecimal amount) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.USER_NOT_FOUND)))
                .flatMap(user -> {
                    BigDecimal newBalance = user.getBalance().add(amount);
                    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new BusinessException(ErrorMessages.INSUFFICIENT_BALANCE));
                    }
                    user.setBalance(newBalance);
                    return userRepository.save(user);
                });
    }
}
