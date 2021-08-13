package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.LeaveApplication.ApplicationEntity;
import lhind.AnnualLeave.LeaveApplication.ApplicationService;
import lhind.AnnualLeave.User.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api")
@AllArgsConstructor
public class RESTController {

    private RegistrationService registrationService;

    private ApplicationService applicationService;

    @PostMapping("signUp/register")
    public String register(@RequestBody UserDTO request){
        return registrationService.register(request);
    }

    @PostMapping("saveApplication")
    public void saveApplication(@RequestBody ApplicationEntity applicationEntity){
        applicationService.saveApplication(applicationEntity);
    }

    @GetMapping(path = "signUp/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
