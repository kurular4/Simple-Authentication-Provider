package com.kurular.simpleauthenticationprovider.autoconfiguration.config;

import com.kurular.simpleauthenticationprovider.autoconfiguration.controller.PublicController;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.MailProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.SimpleAuthenticationProviderProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.PasswordResetTokenRepository;
import com.kurular.simpleauthenticationprovider.autoconfiguration.repository.UserRepository;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.EmailService;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({SimpleAuthenticationProviderProperties.class, MailProperties.class})
@EnableJpaRepositories("com.kurular.simpleauthenticationprovider.autoconfiguration.repository")
@EntityScan("com.kurular.simpleauthenticationprovider.autoconfiguration.model.*")
public class SimpleAuthenticationProviderAutoConfiguration {
    private final SimpleAuthenticationProviderProperties simpleAuthenticationProviderProperties;
    private final MailProperties mailProperties;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final JavaMailSender javaMailSender;

    @Value("classpath:passwordreset-default.html")
    private Resource resource;

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
                getPasswordEncoder(),
                getModelMapper(),
                resource);
    }

    @Bean
    public EmailService getEmailService() {
        return new EmailService(mailProperties, javaMailSender);
    }

    @Bean
    public SecurityConfig getSecurityConfig() {
        return new SecurityConfig(getPasswordEncoder(), getPublicService(), simpleAuthenticationProviderProperties);
    }

    @Bean
    public AsynchronousSpringEventsConfig getAsynchronousSpringEventsConfig() {
        return new AsynchronousSpringEventsConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
