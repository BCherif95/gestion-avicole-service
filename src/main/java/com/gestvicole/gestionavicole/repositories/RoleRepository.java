package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);

    Role findByName(String name);
}
