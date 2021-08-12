package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.LeaveApplication.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class Controllers {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("applications")
    public String getHomePage(Model model){
        model.addAttribute("listApplications", applicationService.getAllApplications());
        return "applications";
    }

    @GetMapping("index")
    public String getIndex(){
        return "index";
    }
}
