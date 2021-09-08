package com.kurular.simpleauthenticationprovider.autoconfiguration.annotation;

import com.kurular.simpleauthenticationprovider.autoconfiguration.config.SimpleAuthenticationProviderAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SimpleAuthenticationProviderAutoConfiguration.class)
public @interface AuthenticationServer {
}
