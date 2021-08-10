package lhind.AnnualLeave.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository <UserEntity, Long>{
    Optional<UserEntity> findByEmail(String email);
}
