package com.kurular.simpleauthenticationprovider.autoconfiguration.controller;

import com.kurular.simpleauthenticationprovider.autoconfiguration.model.Response;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.auth.User;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PublicController {
    private final PublicService publicService;

    @PostMapping("/register")
    public ResponseEntity<Response<User>> register(UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(publicService.register(userDTO));
    }


}
