package DA.backend.repository;

import DA.backend.entity.UserEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEvaluateRepository extends JpaRepository<UserEvaluate,Long> {
    Optional<UserEvaluate> findByUserIdAndEvaluateIdAndQuestionId(String userId, Long evaluateId, Long questionId);
    List<UserEvaluate> findByUserIdAndEvaluateId(String userId, Long evaluateId);
}
