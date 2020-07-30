package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(@Param(value = "email") String email);

    @Query("from User u where u.id=:id")
    User findUserById(@Param(value = "id") Long id);

    boolean existsByUsername(String username);

    User findByUsername(@Param(value = "username") String username);

    @Query("from User u where u.username=:username")
    User checkNewUser(@Param(value = "username") String username);

    @Query("SELECT u.password from User u where u.id=:id")
    String getPwdById(@Param(value = "id") Long id);

    @Query("from User u where u.id<>:id and u.username=:username")
    List<User> checkExistingUser(@Param(value = "id") Long id, @Param(value = "username") String username);

}
