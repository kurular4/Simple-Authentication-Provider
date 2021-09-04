package com.kurular.simpleauthenticationprovider.autoconfiguration.properties;

import com.kurular.simpleauthenticationprovider.autoconfiguration.model.property.Token;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "sap")
public class SimpleAuthenticationProviderProperties {
    private Token token;
}
