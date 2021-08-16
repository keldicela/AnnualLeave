package lhind.AnnualLeave.User;

import lhind.AnnualLeave.Email.EmailSender;
import lhind.AnnualLeave.Email.EmailTemplates;
import lhind.AnnualLeave.Token.ConfirmationToken;
import lhind.AnnualLeave.Token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "User with username %s not found.";
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailTemplates emailTemplates;
    private EmailSender emailSender;


    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public Long getLeaveDays(String email){
        return userRepository.findLeaveDays(email);
    }

    public String signUpUser(UserEntity userEntity){
        boolean userExists = userRepository.findByEmail(userEntity.getEmail()).isPresent();
        if (userExists){
            throw new IllegalStateException("Email already taken.");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);

        String token  = UUID.randomUUID().toString();
        ConfirmationToken confirmation = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userEntity);
        confirmationTokenService.saveConfirmationToken(confirmation);
        return token;
    }

    public UserEntity findUserById(Long id){
        return userRepository.getById(id);
    }

    public UserEntity findUserByEmail(String email){
        return userRepository.getByEmail(email);
    }


    public void updateUser(UserEntity userEntity){
        userRepository.save(userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getUserRole(),
                userEntity.getProbation(),
                userEntity.getLeaveDays());
    }

    public void deleteUser(Long id){
        confirmationTokenService.deleteUserToken(id);
        userRepository.deleteById(id);
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public String sendResetPasswordEmail(ResetPasswordData resetData){
        boolean userExists = userRepository.findByEmail(resetData.getEmail()).isPresent();
        if (!userExists){
            throw new IllegalStateException("No user found with this email." + resetData.getEmail());
        }
        UserEntity user = findUserByEmail(resetData.getEmail());
        String token  = UUID.randomUUID().toString();
        ConfirmationToken confirmation = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenService.saveConfirmationToken(confirmation);

        String link = "http://localhost:7799/signUp/resetPassword?token="+token;
        emailSender.send(
                user.getEmail(),
                emailTemplates.buildResetPasswordEmail(user.getFirstName(), link));
        return "login";
    }

    public String updatePassword(ResetPasswordData data){
        if(!data.getPassword().equals(data.getRepeatPassword())){
            throw new IllegalStateException("Password and repeat password do not match.");
        }

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(data.getToken())
                .orElseThrow(() -> new IllegalStateException("Token not found."));

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token has expired");
        }

        UserEntity user = findUserByEmail(confirmationToken.getUser().getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));

        return "password_success";
    }
}
