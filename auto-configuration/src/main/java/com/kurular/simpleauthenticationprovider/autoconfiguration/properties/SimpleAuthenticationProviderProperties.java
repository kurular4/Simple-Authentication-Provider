package com.kurular.simpleauthenticationprovider.autoconfiguration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Setter
@Getter
@ConfigurationProperties(prefix = "sap")
@Component
public class SimpleAuthenticationProviderProperties {
    private Token token;
    private Set<String> roles;
    private Admin admin;
}
