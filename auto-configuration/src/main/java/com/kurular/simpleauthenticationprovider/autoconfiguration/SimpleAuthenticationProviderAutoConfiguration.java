package com.kurular.simpleauthenticationprovider.autoconfiguration;

import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.SimpleAuthenticationProviderProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@ConditionalOnWebApplication
@ConditionalOnResource(resources = "/model-schema.json")
@EnableConfigurationProperties(SimpleAuthenticationProviderProperties.class)
public class SimpleAuthenticationProviderAutoConfiguration {
    private final SimpleAuthenticationProviderProperties properties;
}
