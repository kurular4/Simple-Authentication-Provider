package com.kurular.simpleauthenticationprovider.autoconfiguration.controller;

import com.kurular.simpleauthenticationprovider.autoconfiguration.model.Response;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(UserDTO userDTO) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/register")
    public ResponseEntity<Response<User>> register(UserDTO userDTO) {
        return ResponseEntity.ok(null);
    }


}
