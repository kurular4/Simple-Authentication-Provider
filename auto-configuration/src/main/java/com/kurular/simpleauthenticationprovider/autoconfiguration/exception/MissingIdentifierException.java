package com.kurular.simpleauthenticationprovider.autoconfiguration.exception;

public class MissingIdentifierException extends RuntimeException {

    public MissingIdentifierException() {
    }

    public MissingIdentifierException(String message) {
        super(message);
    }
}
