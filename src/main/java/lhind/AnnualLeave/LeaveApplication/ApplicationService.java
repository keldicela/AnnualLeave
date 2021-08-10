package lhind.AnnualLeave.LeaveApplication;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

     public void saveApplication(ApplicationRequest applicationRequest){
         System.out.println();
         applicationRepository.save(new LeaveApplication(
                applicationRequest.getEmail(),
                applicationRequest.getDateFrom(),
                applicationRequest.getDateTo()
        ));
    }
}
