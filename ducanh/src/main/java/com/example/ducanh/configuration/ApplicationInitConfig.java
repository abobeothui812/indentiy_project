package com.example.ducanh.configuration;

import com.example.ducanh.entity.Users;
import com.example.ducanh.enums.Roles;
import com.example.ducanh.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
@Slf4j
@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(prefix = "spring",value = "datasource.driver-class-name",
    havingValue = "oracle.jdbc.OracleDriver")
    ApplicationRunner applicationRunner(UserRepository userRepository){
        log.info("Init ApplicationRunner");
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                var roles = new HashSet<String>();
                roles.add(Roles.ADMIN.name());
                Users user = Users.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        //.roles(roles)
                        .build();

                userRepository.save(user);

                log.warn("Admin created default password : admin , please change it");
            }
        };
    }
}
