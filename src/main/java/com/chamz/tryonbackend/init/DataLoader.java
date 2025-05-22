package com.chamz.tryonbackend.init;

import com.chamz.tryonbackend.model.Role;
import com.chamz.tryonbackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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
    }
}
