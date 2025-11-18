package com.petguardian.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Envio de código de verificação (2FA)
    public void send2FACode(String para, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(para);
        message.setSubject("Seu código de verificação (Pet Guardian)");
        message.setText("Seu código de verificação é: " + codigo + "\n\nEle expira em 5 minutos.");
        mailSender.send(message);
    }

    // Envio genérico de e-mails (ex: recuperação de senha)
    public void enviarEmail(String para, String assunto, String corpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(para);
        message.setSubject(assunto);
        message.setText(corpo);
        mailSender.send(message);
    }
}
