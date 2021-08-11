package lhind.AnnualLeave.Email;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirmation Email");
            helper.setFrom("info@anualleave.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
