package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.LeaveApplication.ApplicationEntity;
import lhind.AnnualLeave.LeaveApplication.ApplicationService;
import lhind.AnnualLeave.User.RegistrationService;
import lhind.AnnualLeave.User.UserDTO;
import lhind.AnnualLeave.User.UserEntity;
import lhind.AnnualLeave.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class Controllers {

    private ApplicationService applicationService;

    private UserService userService;

    RegistrationService registrationService;

    @GetMapping("index")
    public String getIndex(){
        return "index";
    }

    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("getApplications")
    public String getApplications(Model model){
        model.addAttribute("listOfApplications", applicationService.getAllApplications());
        return "applications";
    }

    @GetMapping("getUsers")
    public String getUsers(Model model){
        model.addAttribute("listOfUsers", userService.getAllUsers());
        return "users";
    }

    @PostMapping("signUp/register")
    public String register(UserDTO request){
        registrationService.register(request);
        return "users";
    }

    @GetMapping("newUser")
    public String newUserForm(Model model){
        UserEntity userEntity = new UserEntity();
        model.addAttribute("user", userEntity);
        return "new_user";
    }

    @GetMapping("newApplication")
    public String newApplicationForm(Model model){
        ApplicationEntity application = new ApplicationEntity();
        model.addAttribute("application", application);
        return "new_application";
    }

    @PostMapping("saveApplication")
    public String saveApplication(ApplicationEntity applicationEntity){
        applicationService.saveApplication(applicationEntity);
        return "applications";
    }
}
