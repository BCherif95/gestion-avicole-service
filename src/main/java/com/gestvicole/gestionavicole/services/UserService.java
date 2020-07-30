package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.User;
import com.gestvicole.gestionavicole.exception.CustomException;
import com.gestvicole.gestionavicole.repositories.UserRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseBody listOfUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseBody.with(users, users.size() + " utilisateur.s trouve.s");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("An error occured");
        }
    }

    public ResponseBody create(User user) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreateDate(new Date());
            User utilisateur = userRepository.save(user);
            return ResponseBody.with(utilisateur, "Utilisateur ajoutee avec succes");
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseBody edit(User user) {
        try {
            // if empty so => not exist, else => exist
            boolean existingUser = userRepository.checkExistingUser(user.getId(), user.getUsername()).isEmpty();
            if (!existingUser) {
                return ResponseBody.error("Ce nom d'utilisateur existe deja");
            }
            User oldUser = userRepository.findUserById(user.getId());
            if (oldUser == null) {
                return ResponseBody.error("Cet utilisateur n'existe pas");
            }
            if (!StringUtils.isEmpty(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(oldUser.getPassword());
            }
            user.setUpdateDate(new Date());
            userRepository.save(user);
            return ResponseBody.with(user, "Utilisateur modifie avec succes!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est servenue!");
        }
    }

    public ResponseBody remove(Long id) {
        try {
            if (userRepository.findById(id).isPresent()) {
                userRepository.deleteById(id);
            }
            return ResponseBody.success("Suppression avec succes");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est servenue");
        }
    }

    public ResponseBody getUser(Long id) {
        try {
            return ResponseBody.with(userRepository.findUserById(id), "Recuperer avec succes");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Ce nom existe deja!");
        }
    }
}
