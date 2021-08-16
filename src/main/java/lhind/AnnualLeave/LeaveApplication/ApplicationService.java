package lhind.AnnualLeave.LeaveApplication;

import lhind.AnnualLeave.Email.EmailService;
import lhind.AnnualLeave.Email.EmailTemplates;
import lhind.AnnualLeave.User.UserEntity;
import lhind.AnnualLeave.User.UserRepository;
import lhind.AnnualLeave.User.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
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
        Long total = userRepository.getLeaveDays(application.getEmail()) - getWorkingDays(application.getDateFrom(), application.getDateTo());

        if(total < 0){
            throw new IllegalStateException("User does not have enough Leave Days");
        }

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
         Long total = userRepository.getLeaveDays(applicationEntity.getEmail()) - getWorkingDays(applicationEntity.getDateFrom(), applicationEntity.getDateTo());

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
         application.setDays(getWorkingDays(applicationEntity.getDateFrom(), applicationEntity.getDateTo()));
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

        applicationRepository.deleteById(id);
    }

    public long getWorkingDays(LocalDate dayFrom, LocalDate dayTo){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long workingDays = 0;
        try
        {
            Calendar start = Calendar.getInstance();
            start.setTime(sdf.parse(dayFrom.toString()));
            System.out.println("Start date: " + start);
            Calendar end = Calendar.getInstance();
            System.out.println("End date: " + end);

            end.setTime(sdf.parse(dayTo.toString()));
            while(!start.after(end))
            {
                long day = start.get(Calendar.DAY_OF_WEEK);
                if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
                    workingDays++;
                }
                start.add(Calendar.DATE, 1);
            }
            System.out.println(workingDays);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return workingDays;
    }
}
