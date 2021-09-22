package com.kurular.simpleauthenticationprovider.autoconfiguration.repository;

import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
