package com.gestvicole.gestionavicole.config.initData;

import com.gestvicole.gestionavicole.entities.Role;
import com.gestvicole.gestionavicole.entities.User;
import com.gestvicole.gestionavicole.repositories.RoleRepository;
import com.gestvicole.gestionavicole.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(2)
public class initUsers implements ApplicationRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public initUsers(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("Init users ....");

        Role role = roleRepository.findByName("ADMIN");

        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        Set<Role> roles = new HashSet<>(Collections.singletonList(role));
        user.setRoles(roles);
        userService.create(user);

    }
}
