package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Role;
import com.gestvicole.gestionavicole.repositories.RoleRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public ResponseBody getAll() {
        try {
            return ResponseBody.with(roleRepository.findAll(), "Liste de role!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getByName(String name) {
        try {
            return ResponseBody.with(roleRepository.findByName(name),"Role");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody save(Role role) {
        try {
            if (roleRepository.existsByName(role.getName())) {
                roleRepository.save(role);
            }
            return ResponseBody.with(role, "Role modifie avec succes!");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }
    }

}
