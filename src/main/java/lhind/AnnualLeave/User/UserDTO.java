package lhind.AnnualLeave.User;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final UserRole role;
    private final Integer probation;
}
