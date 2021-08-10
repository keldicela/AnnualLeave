package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.Registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("home")
    public String getHomePage(){
        return "home";
    }

}
