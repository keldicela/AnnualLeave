package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.LeaveApplication.ApplicationRepository;
import lhind.AnnualLeave.LeaveApplication.ApplicationRequest;
import lhind.AnnualLeave.LeaveApplication.ApplicationService;
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
public class RESTController {

    private RegistrationService registrationService;

    private ApplicationService applicationService;

    @PostMapping("registration")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @PostMapping("newApplication")
    public void register(@RequestBody ApplicationRequest applicationRequest){
        applicationService.saveApplication(applicationRequest);
    }
}
