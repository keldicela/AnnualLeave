package lhind.AnnualLeave.LeaveApplication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
public class ApplicationRequest {
    private String email;
    private Date dateFrom;
    private Date dateTo;
}
