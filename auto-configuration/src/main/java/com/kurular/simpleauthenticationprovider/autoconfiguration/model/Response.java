package com.kurular.simpleauthenticationprovider.autoconfiguration.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Response<T> {
    private String responseId = UUID.randomUUID().toString();
    private T data;
    private LocalDateTime time;
}
