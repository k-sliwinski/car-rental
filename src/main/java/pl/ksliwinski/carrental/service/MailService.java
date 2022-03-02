package pl.ksliwinski.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.ksliwinski.carrental.model.pojo.VerificationEmail;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(VerificationEmail email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email.getRecipient());
        mailMessage.setSubject(email.getSubject());
        mailMessage.setText(email.getBody());
        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
