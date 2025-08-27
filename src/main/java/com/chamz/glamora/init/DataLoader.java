package com.chamz.glamora.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.chamz.glamora.auth.model.Role;
import com.chamz.glamora.auth.model.User;
import com.chamz.glamora.auth.repository.RoleRepository;
import com.chamz.glamora.auth.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, 
                     UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed roles if they don't exist
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name("ROOT").build());
            roleRepository.save(Role.builder().name("ADMIN").build());
            roleRepository.save(Role.builder().name("CUSTOMER").build());
            roleRepository.save(Role.builder().name("DESIGNER").build());
            roleRepository.save(Role.builder().name("USER").build());
            System.out.println("Default roles inserted.");
        } else {
            System.out.println("Roles already exist. Skipping role seeding.");
        }

        // Seed root user if no users exist
        if(userRepository.count() == 0) {
            Role rootRole = roleRepository.findByName("ROOT")
                .orElseThrow(() -> new RuntimeException("ROOT role not found"));
            
            User rootUser = User.builder()
                .firstName("System")
                .lastName("Administrator")
                .username("Root")
                .email("hasinichamudika@gmail.com")
                .password(passwordEncoder.encode("12345678")) // Make sure to encode the password
                .role(rootRole)
                .build();
            
            userRepository.save(rootUser);
            System.out.println("Root user created successfully.");
        } else {
            System.out.println("Users already exist. Skipping user seeding.");
        }
    }
}