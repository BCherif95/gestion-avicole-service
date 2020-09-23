package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.AuthzRole;
import com.gestvicole.gestionavicole.entities.Privilege;
import com.gestvicole.gestionavicole.entities.Role;
import com.gestvicole.gestionavicole.entities.User;
import com.gestvicole.gestionavicole.exception.CustomException;
import com.gestvicole.gestionavicole.repositories.UserRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

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

    public List<AuthzRole> roles(Long id) {
        ResponseBody body = getUser(Objects.nonNull(id) ? id : null);
        if (Objects.isNull(body) || !body.getStatus().equalsIgnoreCase("Ok")) {
            return emptyList();
        }
        User user = (User) body.getResponse();
        Set<Role> roles = user.getRoles();
        return Objects.isNull(roles) || roles.isEmpty() ? emptyList() : roles
                .stream()
                .findFirst()
                .map(Role::getPrivileges)
                .orElse(emptySet())
                .stream()
                .map(this::exactRole)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(s -> s.split(":")[0]))
                .entrySet()
                .stream()
                .map(this::toAuthzRole)
                .collect(Collectors.toList());
    }

    private AuthzRole toAuthzRole(Map.Entry<String, List<String>> entry) {
        final String role = entry.getKey();
        final Map<String, Boolean> authorizations = entry
                .getValue()
                .stream()
                .map(this::entry)
                .collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
        authorizations.put("list", true);
        return AuthzRole
                .builder()
                .name(role)
                .type("single")
                .authorizations(authorizations)
                .build();
    }

    private SimpleEntry<String, Boolean> entry(String authority) {
        String[] parts = authority.split(":");
        return new SimpleEntry<>(parts[parts.length - 1], true);
    }

    private String exactRole(Privilege privilege) {
        String name = privilege.getName().toLowerCase();
        String[] parts = name.split("_");
        String authority = parts.length > 0 ? parts[0] : null;
        if (Objects.isNull(authority)) {
            return null;
        }
        String role = parts.length > 1 ? stream(parts).skip(1).reduce("", (a, b) -> a.isEmpty() ? b : format("%s_%s", a, b)) : null;
        if (Objects.isNull(role)) {
            return null;
        }
        return format("%s:%s", role, authority);
    }

    private <T> List<T> emptyList() {
        return new ArrayList<>(0);
    }

    private <T> Set<T> emptySet() {
        return new HashSet<>(0);
    }
}
