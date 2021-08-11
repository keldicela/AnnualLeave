package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.LeaveApplication.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

//    @GetMapping("home")
//    public String getHomePage(){
//        return "home";
//    }


    @GetMapping("home")
    public String getHomePage(Model model){
        model.addAttribute("listApplications",applicationService.getAllApplications());
        System.out.println(model.toString());
        return "home";
    }
}
