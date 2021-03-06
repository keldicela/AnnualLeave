package lhind.AnnualLeave.Controller;

import lhind.AnnualLeave.Email.EmailSender;
import lhind.AnnualLeave.Email.EmailService;
import lhind.AnnualLeave.Email.EmailTemplates;
import lhind.AnnualLeave.LeaveApplication.ApplicationEntity;
import lhind.AnnualLeave.LeaveApplication.ApplicationService;
import lhind.AnnualLeave.Token.ConfirmationToken;
import lhind.AnnualLeave.Token.ConfirmationTokenService;
import lhind.AnnualLeave.User.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private RegistrationService registrationService;

    private ConfirmationTokenService confirmationTokenService;

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
    public String register(UserDTO request, Model model){
        registrationService.register(request);
        model.addAttribute("listOfUsers", userService.getAllUsers());
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
    public String updateUser(UserEntity user, Model model) {
        // save employee to database
        userService.updateUser(user);
        model.addAttribute("listOfUsers", userService.getAllUsers());
        return "users";
    }

    @GetMapping("deleteUser/{id}")
    public String deleteUser(@PathVariable (value = "id") Long id, Model model) {
        userService.deleteUser(id);
        model.addAttribute("listOfUsers", userService.getAllUsers());
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
        System.out.println(applicationService.getAllApplications().toString());
        return "applications";
    }

    @GetMapping("getApplicationsByUser/{email}")
    public String getApplicationsById(@PathVariable (value="email") String email, Model model) {
        model.addAttribute("listOfApplications", applicationService.getApplicationsByUser(email));
        model.addAttribute("leaveDays", userService.getLeaveDays(email));
        return "applications";
    }

    @PostMapping("saveApplication")
    public String saveApplication(ApplicationEntity applicationEntity, Model model){
        applicationService.saveApplication(applicationEntity);
        model.addAttribute("listOfApplications", applicationService.getAllApplications());
        return "applications";
    }

    @PostMapping("saveApplicationForUser/{email}")
    public String saveApplication(@PathVariable(value = "email") String email, ApplicationEntity applicationEntity, Model model){
        System.out.println(applicationEntity.getEmail());
        ApplicationEntity application = new ApplicationEntity();
        application.setEmail(email);
        application.setDateFrom(applicationEntity.getDateFrom());
        application.setDateTo(applicationEntity.getDateTo());
        applicationService.saveApplication(application);
        List<ApplicationEntity> userApplications = applicationService.getApplicationsByUser(email);
        model.addAttribute("listOfApplications", userApplications);
        model.addAttribute("leaveDays", userService.getLeaveDays(email));
//        emailService.sendEmailToSupervisors();
        return "applications";
    }

    @GetMapping("deleteApplication/{id}")
    public String deleteApplication(@PathVariable (value = "id") Long id) {
        applicationService.deleteApplicationById(id);
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
        applicationService.saveApplication(applicationEntity);
        return "index";
    }

    @GetMapping("getRequests")
    public String getRequests(Model model){
        model.addAttribute("listOfApplications", applicationService.getApplicationsRequests());
        return "requests";
    }

    @GetMapping("approveRequest/{id}")
    public String approveRequest(@PathVariable (value = "id") Long id, Model model) {
//        System.out.println(id);
        applicationService.approveRequest(id);
        model.addAttribute("listOfApplications", applicationService.getApplicationsRequests());
        return "requests";
    }

    @GetMapping("showDeclineForm/{id}")
    public String showDeclineForm(@PathVariable (value="id") Long id, Model model) {
        ApplicationEntity application = applicationService.findApplicationById(id);
        model.addAttribute("application", application);
        return "decline_request";
    }

    @PostMapping("declineRequest")
    public String declineRequest(ApplicationEntity application, Model model) {
        applicationService.declineRequest(application.getId(), application.getComment());
        model.addAttribute("listOfApplications", applicationService.getApplicationsRequests());
        // save employee to database
        return "requests";
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

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(data.getToken())
                .orElseThrow(() -> new IllegalStateException("Token not found."));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("This token has already been used to reset your password");
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
