package com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Data
@Entity
public class User {
    @Id
    private String id;

    private String username;

    @Email
    private String email;

    @JsonIgnore
    private String password;
}
