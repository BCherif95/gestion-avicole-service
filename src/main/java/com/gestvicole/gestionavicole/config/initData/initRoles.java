package com.gestvicole.gestionavicole.config.initData;

import com.gestvicole.gestionavicole.entities.Role;
import com.gestvicole.gestionavicole.repositories.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class initRoles implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public initRoles(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Role roleAdmin = roleRepository.findByName("ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role("ADMIN");
            roleRepository.save(roleAdmin);
        }

    }
}
