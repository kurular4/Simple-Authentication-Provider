package com.kurular.simpleauthenticationprovider.autoconfiguration;

import com.kurular.simpleauthenticationprovider.autoconfiguration.config.AsynchronousSpringEventsConfig;
import com.kurular.simpleauthenticationprovider.autoconfiguration.config.BeanConfig;
import com.kurular.simpleauthenticationprovider.autoconfiguration.config.SecurityConfig;
import com.kurular.simpleauthenticationprovider.autoconfiguration.controller.PublicController;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.SimpleAuthenticationProviderProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.PasswordResetTokenRepository;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.UserRepository;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.EmailService;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({SimpleAuthenticationProviderProperties.class, MailProperties.class})
@EnableJpaRepositories("com.kurular.simpleauthenticationprovider.autoconfiguration.repository")
@AutoConfigurationPackage(basePackages = {"com.kurular.simpleauthenticationprovider.autoconfiguration.model.*"})
@Import({BeanConfig.class, SecurityConfig.class})
public class SimpleAuthenticationProviderAutoConfiguration {
    private final SimpleAuthenticationProviderProperties simpleAuthenticationProviderProperties;
    private final MailProperties mailProperties;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Value("classpath:passwordreset.html")
    private Resource resource;

    @PostConstruct
    private void postConstruct() {
        if (resource == null) {
            resource = new ClassPathResource("passwordreset-default.html");
        }
    }

    @Bean
    public PublicController getPublicController() {
        return new PublicController(getPublicService());
    }

    @Bean
    public PublicService getPublicService() {
        return new PublicService(simpleAuthenticationProviderProperties,
                applicationEventPublisher,
                passwordResetTokenRepository,
                userRepository,
                passwordEncoder,
                modelMapper,
                resource);
    }

    @Bean
    public EmailService getEmailService() {
        return new EmailService(mailProperties, javaMailSender);
    }

    @Bean
    public AsynchronousSpringEventsConfig getAsynchronousSpringEventsConfig() {
        return new AsynchronousSpringEventsConfig();
    }
}
