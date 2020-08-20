package com.gestvicole.gestionavicole.config.initData;

import com.gestvicole.gestionavicole.entities.Role;
import com.gestvicole.gestionavicole.entities.User;
import com.gestvicole.gestionavicole.repositories.RoleRepository;
import com.gestvicole.gestionavicole.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(2)
public class initUsers implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public initUsers(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Role role = roleRepository.findByName("ADMIN");

        User userAdmin = userRepository.findByUsername("admin");

        if (userAdmin == null) {
            userAdmin = new User();
            userAdmin.setUsername("admin");
            userAdmin.setPassword(passwordEncoder.encode("admin"));
            Set<Role> roles = new HashSet<>(Collections.singletonList(role));
            userAdmin.setRoles(roles);
            userRepository.save(userAdmin);
        }

    }
}
