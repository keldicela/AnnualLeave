package lhind.AnnualLeave.User;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "User with username %s not found.";
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        //TODO: Send confirmation token.
        return "User Registered";
    }
}
