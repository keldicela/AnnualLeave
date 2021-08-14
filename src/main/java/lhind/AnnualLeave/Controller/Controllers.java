package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.LeaveApplication.ApplicationEntity;
import lhind.AnnualLeave.LeaveApplication.ApplicationService;
import lhind.AnnualLeave.User.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class Controllers {

    private ApplicationService applicationService;

    private UserService userService;

    private RegistrationService registrationService;

    @GetMapping("index")
    public String getIndex(){
        return "index";
    }

    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("newUser")
    public String newUserForm(Model model){
        UserEntity userEntity = new UserEntity();
        model.addAttribute("user", userEntity);
        return "new_user";
    }

    @PostMapping("signUp/register")
    public String register(UserDTO request){
        registrationService.register(request);
        return "users";
    }

    @GetMapping("getUsers")
    public String getUsers(Model model){
        model.addAttribute("listOfUsers", userService.getAllUsers());
        return "users";
    }

    @GetMapping("showUpdateUser/{id}")
    public String showUpdateUserForm(@PathVariable (value="id") Long id, Model model) {
        UserEntity user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "update_user";
    }

    @PostMapping("updateUser")
    public String updateUser(UserEntity user) {
        // save employee to database
        userService.updateUser(user);
        return "users";
    }

    @GetMapping("deleteUser/{id}")
    public String deleteUser(@PathVariable (value = "id") Long id) {
        userService.deleteUser(id);
        return "users";
    }

    @GetMapping("newApplication")
    public String newApplicationForm(Model model){
        ApplicationEntity application = new ApplicationEntity();
        model.addAttribute("application", application);
        return "new_application";
    }

    @GetMapping("getApplications")
    public String getApplications(Model model){
        model.addAttribute("listOfApplications", applicationService.getAllApplications());
        return "applications";
    }

    @GetMapping("getApplicationsByUser/{email}")
    public String getApplicationsById(@PathVariable (value="email") String email, Model model) {
        model.addAttribute("listOfApplications", applicationService.getApplicationsByUser(email));
        return "applications";
    }

    @PostMapping("saveApplication")
    public String saveApplication(ApplicationEntity applicationEntity){
        applicationService.saveApplication(applicationEntity);
        return "applications";
    }

    @PostMapping("saveApplicationForUser/{email}")
    public String saveApplication(@PathVariable(value = "email") String email,ApplicationEntity applicationEntity){
        System.out.println(applicationEntity.getEmail());
        ApplicationEntity application = new ApplicationEntity();
        application.setEmail(email);
        application.setDateFrom(applicationEntity.getDateFrom());
        application.setDateTo(applicationEntity.getDateTo());
        applicationService.saveApplication(application);
        return "applications";
    }


    @GetMapping("showUpdateApplication/{id}")
    public String showUpdateApplicationForm(@PathVariable (value = "id") long id, Model model) {
        ApplicationEntity application = applicationService.findApplicationById(id);
        model.addAttribute("application", application);
        return "update_application";
    }

    @PostMapping("updateApplication")
    public String updateApplication(ApplicationEntity applicationEntity) {
        // save employee to database
        applicationService.saveApplication(applicationEntity);
        return "applications";
    }

    @GetMapping("signUp/forgotPassword")
    public String forgotPasswordForm(Model model) {
        ResetPasswordData reset = new ResetPasswordData();
        model.addAttribute("reset", reset);
        return "forgot_password";
    }

    @PostMapping("signUp/sendEmailConfirmation")
    public String forgotPassword(ResetPasswordData reset) {
        return userService.sendResetPasswordEmail(reset);
    }

    @GetMapping("signUp/resetPassword")
    public String resetPassword(@RequestParam("token") String token, final ResetPasswordData data, final Model model){
        if(token.isEmpty()){
            throw new IllegalStateException("Token is empty.");
        }
        ResetPasswordData resetData = new ResetPasswordData();
        resetData.setToken(token);
        setResetPasswordForm(model, resetData);

        return "confirm_password";
    }

    private void setResetPasswordForm(final Model model, final ResetPasswordData data){
        model.addAttribute("resetData", data);
    }

    @PostMapping("signUp/resetPasswordConfirmation")
    public String resetPassword(ResetPasswordData resetData) {
        return userService.updatePassword(resetData);
    }
}
