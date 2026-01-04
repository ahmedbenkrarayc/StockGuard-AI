package com.stockguard.stockguard.security.seeder;

import com.stockguard.stockguard.security.model.User;
import com.stockguard.stockguard.security.model.enums.RoleEnum;
import com.stockguard.stockguard.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UserSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedUsers() {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = User.builder()
                                .role(RoleEnum.ADMIN)
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .active(true)
                                .build();

                User gestionaire = User.builder()
                        .role(RoleEnum.GESTIONNAIRE)
                        .username("gestionaire")
                        .password(passwordEncoder.encode("ahmed123"))
                        .active(true)
                        .build();


                userRepository.saveAll(List.of(
                        admin,
                        gestionaire
                ));
            }
        };
    }
}
