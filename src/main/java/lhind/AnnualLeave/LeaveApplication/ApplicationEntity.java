package lhind.AnnualLeave.LeaveApplication;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@ToString
@Getter
@Setter
@EqualsAndHashCode
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
    private Date dateFrom;
    private Date dateTo;
    private Integer days;
    private String status;

    public ApplicationEntity(String email, Date dateFrom, Date dateTo) {
        this.email = email;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
