package DA.backend.service;

import DA.backend.entity.*;
import DA.backend.projection.UserEvaluateSummaryProjection;
import DA.backend.repository.EvaluateRepository;
import DA.backend.repository.UserEvaluateRepository;
import DA.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEvaluateService {
    @Autowired
    private UserEvaluateRepository userEvaluateRepository;
    @Autowired
    private EvaluateRepository evaluateRepository;
    @Autowired
    private UserRepository userRepository;
    public List<UserEvaluateSummaryProjection> getAllUserEvaluationsByEvaluateId(Long evaluateId) {
        return userEvaluateRepository.findAllUserEvaluationsByEvaluateId(evaluateId);
    }
    public void saveUserEvaluations(List<UserEvaluate> userEvaluates) {
        for (UserEvaluate userEvaluate : userEvaluates) {
            Optional<UserEvaluate> optionalUserEvaluate = userEvaluateRepository.findByUserIdAndEvaluateIdAndQuestionId(
                    userEvaluate.getUser().getId(),
                    userEvaluate.getEvaluate().getId(),
                    userEvaluate.getQuestion().getId()
            );

            if (optionalUserEvaluate.isEmpty()) {
                userEvaluateRepository.save(userEvaluate);
            } else {
                UserEvaluate existingUserEvaluate = optionalUserEvaluate.get();
                existingUserEvaluate.setScore(userEvaluate.getScore());
                existingUserEvaluate.setQuestion(userEvaluate.getQuestion());
                existingUserEvaluate.setEvaluate(userEvaluate.getEvaluate());
                userEvaluateRepository.save(existingUserEvaluate);
            }
        }
    }

    public int calculateTotalScore(String userId, Long evaluateId) {
        return userEvaluateRepository.findByUserIdAndEvaluateId(userId, evaluateId)
                .stream()
                .mapToInt(UserEvaluate::getScore)
                .sum();
    }

    public int calculateTotalScoreAdmin(String userId, Long evaluateId) {
        return userEvaluateRepository.findByUserIdAndEvaluateId(userId, evaluateId)
                .stream()
                .mapToInt(UserEvaluate::getScoreAdmin)
                .sum();
    }

    public int calculateTotalScoreManager(String userId, Long evaluateId) {
        return userEvaluateRepository.findByUserIdAndEvaluateId(userId, evaluateId)
                .stream()
                .mapToInt(UserEvaluate::getScoreManager)
                .sum();
    }

    public List<UserEvaluateSummaryProjection> getUserEvaluateSummaries(Long evaluateId) {
        return userEvaluateRepository.findUserScoresByEvaluation(evaluateId);
    }

    public List<UserEvaluateSummaryProjection> getEvaluationsByEvaluateAndDepartment(Long evaluateId, String userId) {
        return userEvaluateRepository.findEvaluationsByEvaluateAndDepartment(evaluateId, userId);
    }

    public List<UserEvaluateSummaryProjection> getEvaluationsByUserId(String userId) {
        return userEvaluateRepository.findAllEvaluationsForUser(userId);
    }
}
