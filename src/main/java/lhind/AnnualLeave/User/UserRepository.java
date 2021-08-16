package lhind.AnnualLeave.User;

import lhind.AnnualLeave.LeaveApplication.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository <UserEntity, Long>{

    Optional<UserEntity> findByEmail(String email);

    @Query(value = "select ((u.leave_days) - sum(a.days)) as daysleft \n" +
            "from users u, applications a \n" +
            "where a.status = 'APPROVED' \n" +
            "and u.email = a.email \n" +
            "and a.email = :email \n" +
            "group by u.id " , nativeQuery = true)
    Long getLeaveDays(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity a " +
            "SET a.firstName = ?2, a.lastName = ?3, a.email = ?4, a.userRole = ?5, a.probation = ?6, a.leaveDays = ?7 " +
            "WHERE a.id = ?1")
    void save(Long id, String firstName, String lastName, String email, UserRole userRole, Integer probation, Long leaveDays);


    @Transactional
    @Modifying
    @Query("UPDATE UserEntity a " +
            "SET a.leaveDays = ?2 " +
            "WHERE a.id = ?1")
    void updateLeaveDays(Long id, Long leaveDays);

    @Query(value = "SELECT * from users where email = ?1", nativeQuery = true)
    UserEntity getByEmail(String email);

}
