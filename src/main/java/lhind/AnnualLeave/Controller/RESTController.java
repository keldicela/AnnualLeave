package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.LeaveApplication.ApplicationDTO;
import lhind.AnnualLeave.LeaveApplication.ApplicationEntity;
import lhind.AnnualLeave.LeaveApplication.ApplicationService;
import lhind.AnnualLeave.User.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api")
@AllArgsConstructor
public class RESTController {

    private RegistrationService registrationService;

    private ApplicationService applicationService;

    private UserService userService;

    @PostMapping("signUp/register")
    public String register(@RequestBody UserDTO request){
        return registrationService.register(request);
    }

    @PostMapping("saveApplication")
    public void saveApplication(@RequestBody ApplicationDTO applicationDTO){
        applicationService.saveApplication(applicationDTO);
    }

    @GetMapping("getApplications")
    public List<ApplicationEntity> getApplications(){
        return applicationService.getAllApplications();
    }

    @GetMapping("getUsers")
    public List<UserEntity> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(path = "signUp/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
