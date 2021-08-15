package lhind.AnnualLeave.LeaveApplication;

import lhind.AnnualLeave.Email.EmailService;
import lhind.AnnualLeave.Email.EmailTemplates;
import lhind.AnnualLeave.User.UserEntity;
import lhind.AnnualLeave.User.UserRepository;
import lhind.AnnualLeave.User.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Service
public class ApplicationService {

    private UserRepository userRepository;

    private ApplicationRepository applicationRepository;

    private EmailService emailService;

    private EmailTemplates emailTemplate;

    public List<ApplicationEntity> getAllApplications(){
        return applicationRepository.findAll();
    }

    public List<ApplicationEntity> getApplicationsRequests(){
        return applicationRepository.findApplicationsRequests();
    }

    public List<ApplicationEntity> getApplicationsByUser(String email){
        return applicationRepository.findApplicationsByEmail(email);
    }

    public void approveRequest(Long id){
        ApplicationEntity application = findApplicationById(id);

        UserEntity user = userRepository.getByEmail(application.getEmail());
        Long total = user.getLeaveDays() - getWeekDays(application.getDateFrom(), application.getDateTo());

        if(total < 0){
            throw new IllegalStateException("User does not have enough Leave Days");
        }
        else{
            userRepository.save(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserRole(), user.getProbation(), total);}

            applicationRepository.approveRequestById(id);
            emailService.send(user.getEmail(), emailTemplate.buildApprovalEmailForUser(user.getFirstName(), application.getDays()));
    }

    public void declineRequest(Long id, String comment){
        ApplicationEntity application = findApplicationById(id);
        UserEntity user = userRepository.getByEmail(application.getEmail());

        applicationRepository.declineRequestById(id, comment);
        emailService.send(user.getEmail(), emailTemplate.buildDeclineEmailForUser(user.getFirstName(), application.getDays(), comment));
    }

     public void saveApplication(ApplicationEntity applicationEntity){
         UserEntity user = userRepository.getByEmail(applicationEntity.getEmail());
         Long total = user.getLeaveDays() - getWeekDays(applicationEntity.getDateFrom(), applicationEntity.getDateTo());

         if(user.getProbation()<90){
             throw new IllegalStateException("User has less that 90 days of probation.");
         }
         if(total < 0){
             throw new IllegalStateException("User does not have enough Leave Days");
         }

         if(applicationEntity.getStatus()!=null){
             applicationRepository.updateStatus(applicationEntity.getStatus(), applicationEntity.getId());
         }
         else{
         ApplicationEntity application = new ApplicationEntity();
         application.setId(applicationEntity.getId());
         application.setEmail(applicationEntity.getEmail());
         application.setDateFrom(applicationEntity.getDateFrom());
         application.setDateTo(applicationEntity.getDateTo());
         application.setDays(getWeekDays(applicationEntity.getDateFrom(), applicationEntity.getDateTo()));
         application.setStatus(ApplicationStatus.PENDING);

         applicationRepository.save(application);
             List<UserEntity> users = userRepository.findAll();
             for(UserEntity userEntity : users) {
                 if (userEntity.getUserRole().equals(UserRole.SUPERVISOR)) {
                     emailService.send(userEntity.getEmail(), emailTemplate.buildEmailForSupervisor(userEntity.getFirstName()));
                 }
             }
//         emailService.sendEmailToSupervisors();
         }
    }

    public ApplicationEntity findApplicationById(long id){
        return applicationRepository.getById(id);
    }

    public void deleteApplicationById(Long id){
        ApplicationEntity application = findApplicationById(id);
        UserEntity user = userRepository.getByEmail(application.getEmail());
        if(application.getStatus().equals(ApplicationStatus.APPROVED)){
            Long total = user.getLeaveDays() + application.getDays();
            userRepository.save(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserRole(), user.getProbation(), total);
        }
        applicationRepository.deleteById(id);
    }

    public long getWeekDays(LocalDate dayFrom, LocalDate dayTo){

        final long days = (ChronoUnit.DAYS.between(dayFrom, dayTo));
        final long weekDays = days - 2*(days/7); //remove weekends

        return weekDays;
    }
}
