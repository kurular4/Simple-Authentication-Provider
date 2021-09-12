package com.kurular.simpleauthenticationprovider.autoconfiguration.service;

import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.EmailAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.EmailNotFoundException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.MissingIdentifierException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.UsernameAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.PasswordResetToken;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.Role;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.PasswordResetRequestDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.MailProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.SimpleAuthenticationProviderProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.PasswordResetTokenRepository;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class PublicService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MailProperties mailProperties;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final SimpleAuthenticationProviderProperties properties;

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

        try {
            // send email
            JavaMailSender mailSender = new JavaMailSenderImpl();
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            String htmlMsg = "<h3>Hello World!</h3>";
            helper.setText(htmlMsg, true);
            helper.setTo(user.getEmail());
            helper.setSubject("This is the test message for testing gmail smtp server using spring mail");
            helper.setFrom(mailProperties.getUsername());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }


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
