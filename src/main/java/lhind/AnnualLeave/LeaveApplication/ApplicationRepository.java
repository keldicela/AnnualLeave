package lhind.AnnualLeave.LeaveApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    @Query(value = "UPDATE applications a " +
            "SET a.status = :status " +
            "WHERE a.id = :id", nativeQuery = true)
    void updateStatus(ApplicationStatus status, Long id);

    @Query(value = "select * from applications a where a.email = :email", nativeQuery = true)
    List<ApplicationEntity> findApplicationsByEmail(String email);
}

