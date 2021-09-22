package com.kurular.simpleauthenticationprovider.autoconfiguration.service;

import com.kurular.simpleauthenticationprovider.autoconfiguration.model.event.EmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
public class EmailService {
    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(to);
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(mail);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @EventListener
    public void listenEmailEvent(EmailEvent emailEvent) {
        sendEmail(emailEvent.getEmail(), emailEvent.getSubject(), emailEvent.getResource());
    }
}
