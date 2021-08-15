package lhind.AnnualLeave.Email;

import lhind.AnnualLeave.User.UserEntity;
import lhind.AnnualLeave.User.UserRole;
import lhind.AnnualLeave.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.List;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final JavaMailSender mailSender;

//    @Async
//    public void sendEmailToSupervisors(){
//        List<UserEntity> users = userService.getAllUsers();
//        for(UserEntity user : users){
//            if(user.getUserRole().equals(UserRole.SUPERVISOR)){
//                send(user.getEmail(), emailTemplates.buildEmailForSupervisor(user.getFirstName()));
//            }
//        }
//    }

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Annual Leave Application");
            helper.setFrom("info@anualleave.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}