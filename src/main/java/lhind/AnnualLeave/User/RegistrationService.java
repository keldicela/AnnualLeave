package lhind.AnnualLeave.User;

import lhind.AnnualLeave.Registration.EmailValidator;
import lhind.AnnualLeave.User.UserEntity;
import lhind.AnnualLeave.User.UserService;
import lhind.AnnualLeave.User.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;

    public String register(UserDTO user) {
        boolean isValidEmail = emailValidator.test(user.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        return userService.signUpUser(
                new UserEntity(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole(),
                        user.getProbation()));
    }
}
