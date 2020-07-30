package com.gestvicole.gestionavicole.security;

import com.gestvicole.gestionavicole.entities.Role;
import com.gestvicole.gestionavicole.entities.User;
import com.gestvicole.gestionavicole.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Set;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        String[] userRoles = roles.stream().map(Role::getAuthority).toArray(String[]::new);
        // System.out.println("ROLES : " + Arrays.toString(userRoles));
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        // System.out.println("loadUserByUsername : " + username);
        final User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Username : " + username + " not found");
        // if (!user.isEnabled()) throw new UsernameNotFoundException("User : " + username + " is not enable");
        // return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
        return org.springframework.security.core.userdetails.User//
                .withUsername(user.getUsername())//
                .password(user.getPassword())
                .authorities(getAuthorities(user.getRoles()))
                .accountLocked(!user.isEnabled())
                .disabled(!user.isEnabled())
                .build();
    }
}
