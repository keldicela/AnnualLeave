package lhind.AnnualLeave.LeaveApplication;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

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
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private String comment;

}
