package com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String email;
    private String password;
}
