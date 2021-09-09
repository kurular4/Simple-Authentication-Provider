package com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;
}
