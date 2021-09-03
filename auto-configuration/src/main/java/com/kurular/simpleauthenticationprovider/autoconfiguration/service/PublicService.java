package com.kurular.simpleauthenticationprovider.autoconfiguration.service;

import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.EmailAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.UsernameAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.Response;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class PublicService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public Response<User> register(UserDTO userDTO) throws Exception {
        if (!userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(userDTO.getUsername());
        }

        if (!userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }

        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Response<User> response = new Response<>();
        response.setData(userRepository.save(user));
        return response;
    }
}
