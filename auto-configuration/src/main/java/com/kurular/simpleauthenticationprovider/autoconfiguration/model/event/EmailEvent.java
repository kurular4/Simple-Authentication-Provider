package com.kurular.simpleauthenticationprovider.autoconfiguration.model.event;


import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailEvent extends ApplicationEvent {
    private final String email;
    private final String subject;
    private final String resource;

    public EmailEvent(Object source, String email, String subject, String resource) {
        super(source);
        this.email = email;
        this.subject = subject;
        this.resource = resource;
    }
}
