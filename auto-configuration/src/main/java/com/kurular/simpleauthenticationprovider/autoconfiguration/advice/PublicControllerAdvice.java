package com.kurular.simpleauthenticationprovider.autoconfiguration.advice;

import com.kurular.simpleauthenticationprovider.autoconfiguration.exception.UsernameAlreadyExistsException;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PublicControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<Response<String>> handleUsernameAlreadyExists(UsernameAlreadyExistsException exception) {
        Response<String> response = new Response<>();
        response.setData(exception.getMessage());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler
    public ResponseEntity<Response<String>> handleEmailAlreadyExists(UsernameAlreadyExistsException exception) {
        Response<String> response = new Response<>();
        response.setData(exception.getMessage());
        return ResponseEntity.ok(response);
    }
}
