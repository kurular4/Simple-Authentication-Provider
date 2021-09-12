package com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDTO {
    private String email;
    private String username;
    private String html;
}
