package com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String email;
    private String password;
}
