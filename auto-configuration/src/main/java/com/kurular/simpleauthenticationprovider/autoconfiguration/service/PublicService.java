package com.kurular.simpleauthenticationprovider.autoconfiguration.service;

import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PublicService {
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public ResponseEntity<User> register(UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(Optional.of(userDTO)
                .map(u -> modelMapper.map(u, User.class))
                .map(user -> {
                    user.setId(UUID.randomUUID().toString());
                    user.setPassword(passwordEncoder.encode(user.getPassword()));

                    // check email and username if they exist

                    // save
                    return user;
                }).orElseThrow(Exception::new));
    }
}
