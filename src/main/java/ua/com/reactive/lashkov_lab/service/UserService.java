package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.exception.BusinessException;
import ua.com.reactive.lashkov_lab.exception.ErrorMessages;
import ua.com.reactive.lashkov_lab.repository.RoleRepository;
import ua.com.reactive.lashkov_lab.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .cast(UserDetails.class);
    }

    public Mono<User> registerUser(UserRegistrationDto registrationDto) {
        return userRepository.findByUsername(registrationDto.getUsername())
                .flatMap(existingUser -> Mono.<User>error(new BusinessException(ErrorMessages.USERNAME_EXISTS)))
                .switchIfEmpty(roleRepository.findById(1L)
                        .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DEFAULT_ROLE_NOT_FOUND)))
                        .flatMap(role -> {
                            User newUser = new User();
                            newUser.setUsername(registrationDto.getUsername());
                            newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
                            newUser.setBalance(0.0);
                            newUser.setRole(role);
                            return userRepository.save(newUser);
                        }));
    }

    public Mono<User> updateUserBalance(Long userId, double amount) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.USER_NOT_FOUND)))
                .flatMap(user -> {
                    if (amount < 0 && Math.abs(amount) > user.getBalance()) {
                        return Mono.error(new BusinessException(ErrorMessages.INSUFFICIENT_BALANCE));
                    }
                    user.setBalance(user.getBalance() + amount);
                    return userRepository.save(user);
                });
    }
}
