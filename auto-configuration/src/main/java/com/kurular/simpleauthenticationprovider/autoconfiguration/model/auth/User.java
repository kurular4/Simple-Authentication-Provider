package com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;

    private String username;

    @Email
    private String email;

    @JsonIgnore
    private String password;

    @ElementCollection
    private List<String> roles;
}
