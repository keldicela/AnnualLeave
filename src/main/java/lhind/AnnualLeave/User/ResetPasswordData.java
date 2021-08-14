package lhind.AnnualLeave.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordData {
    private String email;
    private String token;
    private String password;
    private String repeatPassword;
}
