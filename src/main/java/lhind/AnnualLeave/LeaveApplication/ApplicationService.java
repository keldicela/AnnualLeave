package lhind.AnnualLeave.LeaveApplication;

import lhind.AnnualLeave.User.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ApplicationService {

    private UserRepository userRepository;

    private ApplicationRepository applicationRepository;

    public List<ApplicationEntity> getAllApplications(){
        return applicationRepository.findAll();
    }

     public void saveApplication(ApplicationDTO applicationDTO){
        Integer repo = userRepository.getProbation(applicationDTO.getEmail());
         if(repo<90){
             throw new IllegalStateException("User has less that 100 days of probation.");
         }
             System.out.println(repo);

//         final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
//         final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//
//         final LocalDate firstDate = LocalDate.parse(applicationRequest.getDateFrom().toString(), formatter);
//         final LocalDate secondDate = LocalDate.parse(applicationRequest.getDateTo().toString(), formatter);
//         final long days = ChronoUnit.DAYS.between(firstDate, secondDate);

         applicationRepository.save(new ApplicationEntity(
                applicationDTO.getEmail(),
                applicationDTO.getDateFrom(),
                applicationDTO.getDateTo()
        ));
    }
}
