package lhind.AnnualLeave.LeaveApplication;

import lhind.AnnualLeave.User.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

     public void saveApplication(ApplicationEntity applicationEntity){
        Integer repo = userRepository.getProbation(applicationEntity.getEmail());
         if(repo<90){
             throw new IllegalStateException("User has less that 90 days of probation.");
         }

         final long days = (ChronoUnit.DAYS.between(applicationEntity.getDateFrom(), applicationEntity.getDateTo()))+1;
         final long weekDays = days - 2*(days/7); //remove weekends

         applicationRepository.save(new ApplicationEntity(
                 applicationEntity.getEmail(),
                 applicationEntity.getDateFrom(),
                 applicationEntity.getDateTo(),
                 weekDays
        ));
    }
}
