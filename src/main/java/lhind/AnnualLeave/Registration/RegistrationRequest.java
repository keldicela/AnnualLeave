package lhind.AnnualLeave.Registration;

import lhind.AnnualLeave.AppUser.AppUserRole;
import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private final String username;
    private final String email;
    private final String password;
    private final AppUserRole role;
}
