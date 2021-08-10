package lhind.AnnualLeave.LeaveApplication;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ApplicationService {

    private ApplicationRepository applicationRepository;

     public void saveApplication(ApplicationRequest applicationRequest){

//         final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
//         final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//
//         final LocalDate firstDate = LocalDate.parse(applicationRequest.getDateFrom().toString(), formatter);
//         final LocalDate secondDate = LocalDate.parse(applicationRequest.getDateTo().toString(), formatter);
//         final long days = ChronoUnit.DAYS.between(firstDate, secondDate);

         applicationRepository.save(new ApplicationEntity(
                applicationRequest.getEmail(),
                applicationRequest.getDateFrom(),
                applicationRequest.getDateTo()
        ));
    }
}
