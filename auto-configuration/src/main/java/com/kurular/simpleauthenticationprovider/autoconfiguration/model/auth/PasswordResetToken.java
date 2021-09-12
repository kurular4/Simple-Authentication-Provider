package com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
public class PasswordResetToken implements Serializable {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    public PasswordResetToken(User user, String token) {
        this.user = user;
        this.token = token;
        this.expiryDate = Date.from(Instant.now().plus(Duration.ofMillis(EXPIRATION)));
    }
}
