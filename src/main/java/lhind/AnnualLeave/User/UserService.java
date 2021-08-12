package lhind.AnnualLeave.User;

import lhind.AnnualLeave.Token.ConfirmationToken;
import lhind.AnnualLeave.Token.ConfirmationTokenRepository;
import lhind.AnnualLeave.Token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
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


    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
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

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }
}
