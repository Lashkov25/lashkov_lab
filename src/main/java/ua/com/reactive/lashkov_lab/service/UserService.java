package ua.com.reactive.lashkov_lab.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.AuthRequestDto;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.entity.RolesHasUsers;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.repository.RoleRepository;
import ua.com.reactive.lashkov_lab.repository.RolesHasUsersRepository;
import ua.com.reactive.lashkov_lab.repository.UserRepository;

@Service
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RolesHasUsersRepository rolesHasUsersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
                      RoleRepository roleRepository,
                      RolesHasUsersRepository rolesHasUsersRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rolesHasUsersRepository = rolesHasUsersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .cast(UserDetails.class);
    }

    public Mono<User> authenticateUser(AuthRequestDto authRequest) {
        return userRepository.findByUsername(authRequest.getUsername())
                .filter(user -> passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username or password")));
    }

    @Transactional
    public Mono<User> createUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setName(registrationDto.getName());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setBalance(0.0);

        return userRepository.save(user)
                .flatMap(savedUser -> 
                    roleRepository.findByName("USER")
                        .flatMap(role -> {
                            RolesHasUsers rolesHasUsers = new RolesHasUsers();
                            rolesHasUsers.setUserId(savedUser.getId());
                            rolesHasUsers.setRoleId(role.getId());
                            return rolesHasUsersRepository.save(rolesHasUsers)
                                    .thenReturn(savedUser);
                        })
                );
    }

    @Transactional
    public Mono<User> registerUser(User user) {
        return Mono.just(user)
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(u.getPassword()));
                    if (u.getName() == null) {
                        u.setName(u.getUsername());
                    }
                    return u;
                })
                .flatMap(userRepository::save)
                .flatMap(savedUser -> 
                    roleRepository.findByName("USER")
                        .flatMap(role -> {
                            RolesHasUsers rolesHasUsers = new RolesHasUsers();
                            rolesHasUsers.setUserId(savedUser.getId());
                            rolesHasUsers.setRoleId(role.getId());
                            return rolesHasUsersRepository.save(rolesHasUsers)
                                    .thenReturn(savedUser);
                        })
                );
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Mono<Void> deleteUser(Long id) {
        return rolesHasUsersRepository.deleteAllByUserId(id)
                .then(userRepository.deleteById(id));
    }
}
