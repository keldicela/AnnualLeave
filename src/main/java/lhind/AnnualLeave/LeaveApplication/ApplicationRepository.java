package lhind.AnnualLeave.LeaveApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    @Query(value = "UPDATE applications a " +
            "SET a.status = :status " +
            "WHERE a.id = :id", nativeQuery = true)
    void updateStatus(ApplicationStatus status, Long id);

    @Query(value = "select * from applications a where a.email = :email", nativeQuery = true)
    List<ApplicationEntity> findApplicationsByEmail(String email);

    @Query(value = "select * from applications a where status = 'PENDING' ", nativeQuery = true)
    List<ApplicationEntity> findApplicationsRequests();

    @Transactional
    @Modifying
    @Query("UPDATE ApplicationEntity a " +
            "SET a.status = 'APPROVED' WHERE a.id = ?1")
    void approveRequestById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE ApplicationEntity a " +
            "SET a.status = 'DECLINED', a.comment= ?2 WHERE a.id = ?1")
    void declineRequestById(Long id, String comment);
}

