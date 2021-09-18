package com.kurular.simpleauthenticationprovider.autoconfiguration.service;

import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.EmailAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.EmailNotFoundException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.UsernameAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.PasswordResetToken;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.PasswordChangeDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.PasswordResetRequestDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.event.EmailEvent;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.SimpleAuthenticationProviderProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.PasswordResetTokenRepository;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.UserRepository;
import com.kurular.simpleauthenticationprovider.autoconfiguration.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class PublicService implements UserDetailsService {
    private final SimpleAuthenticationProviderProperties properties;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Value("classpath:passwordreset-default.html")
    private Resource resource;

    public User register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(userDTO.getUsername());
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }

        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add(properties.getRoles().iterator().next());
        user.setRoles(roles);

        userRepository.save(user);
        return user;
    }

    // todo enhance return type
    public String resetPassword(PasswordResetRequestDTO passwordResetRequestDTO) {
        User user = (User) loadByEmail(passwordResetRequestDTO.getEmail());

        String token = UUID.randomUUID().toString();
        passwordResetTokenRepository.save(new PasswordResetToken(user, token));

        String resourceString = FileUtil.asString(resource).replace("${RESET_TOKEN}", token);

        EmailEvent emailEvent = new EmailEvent(this, user.getEmail(),
                passwordResetRequestDTO.getMailSubject(), resourceString);
        applicationEventPublisher.publishEvent(emailEvent);
        return "Password reset instructions sent";
    }

    // todo enhance return type
    public String changePassword(PasswordChangeDTO passwordChangeDTO) {
        passwordResetTokenRepository
                .findByToken(passwordChangeDTO.getPasswordResetToken())
                .filter(passwordResetToken -> {
                    if (passwordResetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                        throw new RuntimeException();
                    }
                    return true;
                })
                .map(PasswordResetToken::getUser)
                .ifPresent(user -> {
                    if (!passwordEncoder.matches(user.getPassword(), passwordChangeDTO.getOldPassword())) {
                        throw new RuntimeException();
                    }
                    user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
                    userRepository.save(user);
                });

        return "Password changed successfully";
    }

    public UserDetails loadByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        return userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }
}
