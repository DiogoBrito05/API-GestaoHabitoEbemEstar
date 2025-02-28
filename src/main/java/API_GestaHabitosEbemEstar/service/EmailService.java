package API_GestaHabitosEbemEstar.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${spring.mail.from}")
    private String systemEmail;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Método que envia e-mails
    // Cria uma nova mensagem de e-mail
    // Auxilia na montagem da mensagem
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(systemEmail);// remetente do emails
            helper.setTo(to); // Define o destinatário do e-mail
            helper.setSubject(subject); // Define o assunto do e-mail
            helper.setText(body, true); // Define o corpo do e-mail (true indica que aceita HTML)

            // Envia o e-mail
            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}
