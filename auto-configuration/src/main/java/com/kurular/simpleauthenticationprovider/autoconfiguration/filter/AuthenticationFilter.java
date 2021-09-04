package com.kurular.simpleauthenticationprovider.autoconfiguration.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurular.simpleauthenticationprovider.autoconfiguration.model.dto.UserDTO;
import com.kurular.simpleauthenticationprovider.autoconfiguration.properties.SimpleAuthenticationProviderProperties;
import com.omer.jwtutils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final SimpleAuthenticationProviderProperties properties;

    public AuthenticationFilter(AuthenticationManager authenticationManager, SimpleAuthenticationProviderProperties properties) {
        this.authenticationManager = authenticationManager;
        this.properties = properties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDTO userDTO = mapper.readValue(request.getInputStream(), UserDTO.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtUtil.getAuthorizationHeader(), authResult.getAuthorities());
        String token = JwtUtil.createToken(authResult.getName(), claims, properties.getToken().getDuration(), properties.getToken().getSecretKey());
        response.addHeader(JwtUtil.getAuthorizationHeader(), JwtUtil.getTokenPrefix() + " " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        new ObjectMapper().writeValue(response.getOutputStream(), new ResponseEntity<>("", HttpStatus.BAD_REQUEST));
    }


}
