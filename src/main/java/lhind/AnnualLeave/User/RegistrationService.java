package lhind.AnnualLeave.User;

import lhind.AnnualLeave.Email.EmailSender;
import lhind.AnnualLeave.Email.EmailTemplates;
import lhind.AnnualLeave.Token.ConfirmationToken;
import lhind.AnnualLeave.Token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private EmailTemplates emailTemplates;

    public String register(UserDTO user) {
        String token = userService.signUpUser(
                new UserEntity(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getUserRole(),
                        user.getProbation(),
                        user.getLeaveDays()));

        String link = "http://localhost:7799/api/signUp/confirm?token=" + token;
        emailSender.send(user.getEmail(), emailTemplates.buildRegistrationEmail(user.getFirstName(), link));
        return token;
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "Email confirmed!";
    }
}
