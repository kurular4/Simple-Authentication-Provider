package com.kurular.simpleauthenticationprovider.autoconfiguration.controller;

import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.PasswordChangeDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.PasswordResetRequestDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PublicController {
    private final PublicService publicService;

    @PutMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(publicService.register(userDTO));
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {
        return ResponseEntity.ok(publicService.resetPassword(passwordResetRequestDTO));
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        return ResponseEntity.ok(publicService.changePassword(passwordChangeDTO));
    }
}
