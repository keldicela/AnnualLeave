package lhind.AnnualLeave.LeaveApplication;

import lhind.AnnualLeave.User.UserRepository;
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

    public List<ApplicationEntity> getAllApplications(){
        return applicationRepository.findAll();
    }

    public List<ApplicationEntity> getApplicationsByUser(String email){
        return applicationRepository.findApplicationsByEmail(email);
    }

     public void saveApplication(ApplicationEntity applicationEntity){
        Integer repo = userRepository.getProbation(applicationEntity.getEmail());
         if(repo<90){
             throw new IllegalStateException("User has less that 90 days of probation.");
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
         application.setDays(getWeekDays(applicationEntity.getDateFrom(), applicationEntity.getDateTo()));
         application.setStatus(ApplicationStatus.PENDING);

         applicationRepository.save(application);
         }
    }

    public ApplicationEntity findApplicationById(long id){
        return applicationRepository.getById(id);
    }

    public long getWeekDays(LocalDate dayFrom, LocalDate dayTo){
        final long days = (ChronoUnit.DAYS.between(dayFrom, dayTo))+1;
        final long weekDays = days - 2*(days/7); //remove weekends
        return weekDays;
    }
}
