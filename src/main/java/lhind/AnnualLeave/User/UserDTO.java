package lhind.AnnualLeave.User;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class UserDTO {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final UserRole userRole;
    private final Integer probation;
    private final Long leaveDays;
}
