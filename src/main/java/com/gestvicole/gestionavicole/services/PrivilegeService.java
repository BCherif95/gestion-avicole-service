package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.repositories.PrivilegeRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService {

    final PrivilegeRepository privilegeRepository;

    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    public ResponseBody getAll() {
        try {
            return ResponseBody.with(privilegeRepository.findAll(), "Liste de privilige!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }
}
