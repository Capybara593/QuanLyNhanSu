package DA.backend.service;

import DA.backend.entity.*;
import DA.backend.repository.EvaluateRepository;
import DA.backend.repository.QuestionSetRepository;
import DA.backend.repository.UserEvaluateRepository;
import DA.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEvaluateService {
    @Autowired
    UserEvaluateRepository userEvaluateRepository;
    @Autowired
    EvaluateRepository evaluateRepository;
    @Autowired
    QuestionSetRepository questionSetRepository;
    @Autowired
    UserRepository userRepository;

    public void DG(String userId, Long evaluateId, List<Question> questions) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Evaluate> optionalEvaluate = evaluateRepository.findById(evaluateId);

        if (optionalUser.isPresent() && optionalEvaluate.isPresent()) {
            User user = optionalUser.get();
            Evaluate evaluate = optionalEvaluate.get();

            for (Question question : questions) {
                Optional<UserEvaluate> existingUserEvaluate = userEvaluateRepository
                        .findByUserIdAndEvaluateIdAndQuestionId(user.getId(), evaluate.getId(), question.getId());

                UserEvaluate userEvaluate;
                if (existingUserEvaluate.isPresent()) {
                    userEvaluate = existingUserEvaluate.get();
                    userEvaluate.setScore(question.getCore());
                } else {
                    userEvaluate = new UserEvaluate();
                    UserEvaluateId userEvaluateId = new UserEvaluateId();
                    userEvaluateId.setEvaluateId(evaluate.getId());
                    userEvaluateId.setUserId(user.getId());
                    userEvaluate.setId(userEvaluateId);
                    userEvaluate.setScore(question.getCore());
                }
                userEvaluateRepository.save(userEvaluate);
            }
        }
    }


    // Phương thức tính tổng điểm đánh giá cho một User trong một Evaluate
    public int calculateTotalScore(String userId, Long evaluateId) {
        // Tìm tất cả các UserEvaluate cho userId và evaluateId
        List<UserEvaluate> userEvaluates = userEvaluateRepository.findByUserIdAndEvaluateId(userId, evaluateId);

        // Tính tổng điểm
        int totalScore = userEvaluates.stream()
                .mapToInt(UserEvaluate::getScore)
                .sum();

        return totalScore;
    }
    public List<User> listUser(Long evaluateId){
        Optional<Evaluate> optionalEvaluate = evaluateRepository.findById(evaluateId);
        if(optionalEvaluate.isPresent()){
            Evaluate evaluate = optionalEvaluate.get();
            // Lấy danh sách User từ danh sách UserEvaluate
            return evaluate.getUserEvaluates().stream()
                    .map(UserEvaluate::getUser) // Lấy User từ mỗi UserEvaluate
                    .collect(Collectors.toList()); // Chuyển đổi thành List<User>
        }
        return  null;
    }

}
