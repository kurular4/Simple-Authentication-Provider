package com.kurular.simpleauthenticationprovider.autoconfiguration.config;

import com.kurular.simpleauthenticationprovider.autoconfiguration.controller.PublicController;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.MailProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.SimpleAuthenticationProviderProperties;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.EmailService;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@RequiredArgsConstructor
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({SimpleAuthenticationProviderProperties.class, MailProperties.class})
@EnableJpaRepositories("com.kurular.simpleauthenticationprovider.autoconfiguration.repository")
@EntityScan("com.kurular.simpleauthenticationprovider.autoconfiguration.model.*")
@Import({PublicController.class, PublicService.class, EmailService.class, BeanConfig.class, SecurityConfig.class, AsynchronousSpringEventsConfig.class})
public class SimpleAuthenticationProviderAutoConfiguration {

}
