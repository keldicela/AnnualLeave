package lhind.AnnualLeave.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

//
//    @Query(value = "UPDATE USERS C " +
//            "SET C.PASSWORD = :email " +
//            "WHERE c.token = ?1", nativeQuery = true)
//    public int updateConfirmedAt(String token);
}
