package com.kurular.simpleauthenticationprovider.autoconfiguration.config;

import com.kurular.simpleauthenticationprovider.autoconfiguration.controller.PublicController;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.MailProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.SimpleAuthenticationProviderProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.EmailService;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({SimpleAuthenticationProviderProperties.class, MailProperties.class})
@EnableJpaRepositories("com.kurular.simpleauthenticationprovider.autoconfiguration.repository")
@EntityScan("com.kurular.simpleauthenticationprovider.autoconfiguration.model.*")
@Import({PublicController.class, PublicService.class, EmailService.class, SecurityConfig.class})
public class SimpleAuthenticationProviderAutoConfiguration {
    private final SimpleAuthenticationProviderProperties simpleAuthenticationProviderProperties;

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
