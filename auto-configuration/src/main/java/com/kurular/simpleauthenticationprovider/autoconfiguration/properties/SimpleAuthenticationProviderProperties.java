package com.kurular.simpleauthenticationprovider.autoconfiguration.properties;

import com.kurular.simpleauthenticationprovider.autoconfiguration.model.Token;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sap")
public class SimpleAuthenticationProviderProperties {
    private Token token;
}
