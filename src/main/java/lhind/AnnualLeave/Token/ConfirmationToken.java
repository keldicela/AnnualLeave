package lhind.AnnualLeave.Token;

import lhind.AnnualLeave.User.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ConfirmationToken {
    @SequenceGenerator(
            name = "token_sequence",
            sequenceName = "token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )
    @Id
    private Long id;
    private String token;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name="user_id"
    )
    private UserEntity user;

    public ConfirmationToken(String token, UserEntity userEntity) {
        this.token = token;
        this.user = userEntity;
    }
}
