package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.Registration.RegistrationRequest;
import lhind.AnnualLeave.Registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api")
@AllArgsConstructor
public class UserRegistrationController {

    private RegistrationService registrationService;

    @PostMapping("registration")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

}
