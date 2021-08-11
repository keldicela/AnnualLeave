package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.LeaveApplication.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controllers {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("home")
    public String getHomePage(Model model){
        model.addAttribute("listApplications", applicationService.getAllApplications());
        return "home";
    }

    @GetMapping("index")
    public String getIndex(Model model){
        return "index";
    }
}
