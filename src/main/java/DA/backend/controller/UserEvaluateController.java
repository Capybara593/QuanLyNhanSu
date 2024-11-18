package DA.backend.controller;

import DA.backend.entity.Question;
import DA.backend.entity.User;
import DA.backend.service.UserEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userEvaluate")
public class UserEvaluateController {

    @Autowired
    private UserEvaluateService userEvaluateService;

    // Endpoint để đánh giá
    @PostMapping("/evaluate")
    public ResponseEntity<Void> evaluateUser(@RequestParam String userId, @RequestParam Long evaluateId, @RequestBody List<Question> questions) {
        userEvaluateService.DG(userId, evaluateId, questions);
        return ResponseEntity.ok().build();
    }

    // Endpoint để tính tổng điểm đánh giá của một User trong một Evaluate
    @GetMapping("/calculateTotalScore")
    public ResponseEntity<Integer> calculateTotalScore(@RequestParam String userId, @RequestParam Long evaluateId) {
        int totalScore = userEvaluateService.calculateTotalScore(userId, evaluateId);
        return ResponseEntity.ok(totalScore);
    }

    // Endpoint để lấy danh sách User đã được đánh giá trong một Evaluate
    @GetMapping("/listUser")
    public ResponseEntity<List<User>> listUsers(@RequestParam Long evaluateId) {
        List<User> users = userEvaluateService.listUser(evaluateId);
        return ResponseEntity.ok(users);
    }
}
