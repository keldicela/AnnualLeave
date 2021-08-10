package lhind.AnnualLeave.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("home")
    public String getHomePage(){
        return "home";
    }
}
