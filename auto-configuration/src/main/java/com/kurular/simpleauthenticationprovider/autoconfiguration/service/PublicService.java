package com.kurular.simpleauthenticationprovider.autoconfiguration.service;

import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.EmailAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.EmailNotFoundException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.MissingIdentifierException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.UsernameAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.Response;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.PasswordResetToken;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.Role;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.PasswordResetRequestDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.PasswordResetTokenRepository;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class PublicService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public User register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(userDTO.getUsername());
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }

        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        userRepository.save(user);
        return user;
    }

    public String resetPassword(PasswordResetRequestDTO passwordResetRequestDTO) {
        User user;

        if (passwordResetRequestDTO.getEmail() != null) {
            user = (User) loadByEmail(passwordResetRequestDTO.getEmail());
        } else if (passwordResetRequestDTO.getUsername() != null) {
            user = (User) loadUserByUsername(passwordResetRequestDTO.getUsername());
        } else {
            throw new MissingIdentifierException();
        }

        if (user == null) {
            throw new RuntimeException();
        }

        String token = UUID.randomUUID().toString();
        createPasswordResetToken(user, token);
        // send email
        return "Reset token sent successfully";
    }

    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public UserDetails loadByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        return userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }
}
