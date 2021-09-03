package com.kurular.simpleauthenticationprovider.autoconfiguration.exception;

public class EmailAlreadyExistsException extends Exception {

    public EmailAlreadyExistsException() {
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
