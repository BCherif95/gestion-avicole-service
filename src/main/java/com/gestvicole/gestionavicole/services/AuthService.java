package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.User;
import com.gestvicole.gestionavicole.repositories.UserRepository;
import com.gestvicole.gestionavicole.security.jwt.JwtTokenProvider;
import com.gestvicole.gestionavicole.utils.AuthBody;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * @param username
     * @param password
     * @desc user's authentication
     */
    public ResponseBody login(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
                return ResponseBody.error("Vos identifiants sont incorrects");
            }

            if (!user.isEnabled()) {
                return ResponseBody.error("Vous n'etes plus autorise a vous connecter.\n"
                        + "Veuillez contactez l'administrateur SVP.");
            }

            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (auth == null) {
                return ResponseBody.error("AUTH IS NULL");
            }
            user.setPassword(null);

            return ResponseBody.with(new AuthBody(user, jwtTokenProvider.createToken(user)), "Vous etes authentifie avec succes");

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return ResponseBody.error("An error occured");
        }
    }

    /**
     * @param authBody
     * @return
     * @desc change user password
     */
    public ResponseBody updatePassword(AuthBody authBody) {
        try {
            User user = userRepository.findUserById(authBody.getUserId());
            if (user == null) {
                return ResponseBody.error("Cet utilisateur n'existe pas");
            }
            if (!passwordEncoder.matches(authBody.getOldPassword(), user.getPassword())) {
                return ResponseBody.error("Votre ancien mot de passe est incorrect");
            }
            user.setPassword(passwordEncoder.encode(authBody.getNewPassword()));
            userRepository.save(user);

            return ResponseBody.success("Votre mot de passe a ete change avec succes");

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return ResponseBody.error("An error occured");
        }
    }

    public ResponseBody resetPassword(Long id, String newPassword) {
        try {
            User user = userRepository.findUserById(id);
            if (user == null) {
                return ResponseBody.error("Cet utilisateur n'existe pas");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            return ResponseBody.with(user, "Le mot de passe a ete reinitialise avec succes");

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return ResponseBody.error("An error occured");
        }
    }

    public ResponseBody whoIs(HttpServletRequest req) {
        try {
            User user = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
            if (user == null) {
                return ResponseBody.error("Invalide token/session");
            }

            return ResponseBody.with(user, "Your are this user");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return ResponseBody.error("An error occured");
        }
    }

    public ResponseBody refreskToken(String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseBody.error("Invalide username");
            }
            return ResponseBody.with(jwtTokenProvider.createToken(user), "New generated token");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return ResponseBody.error("An error occured");
        }
    }
}
