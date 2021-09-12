package com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDTO {
    private String oldPassword;
    private String newPassword;
    private String passwordResetToken;
}
