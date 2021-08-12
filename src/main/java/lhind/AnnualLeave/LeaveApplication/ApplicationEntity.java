package lhind.AnnualLeave.LeaveApplication;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class ApplicationEntity {
    @SequenceGenerator(
            name = "application_sequence",
            sequenceName = "application_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "application_sequence"
    )
    @Id
    private Long id;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;
    private Long days;
    private String status;

    public ApplicationEntity(String email, LocalDate dateFrom, LocalDate dateTo, Long days) {
        this.email = email;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.days = days;
    }
}
