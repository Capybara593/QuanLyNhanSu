package DA.backend.controller;

import DA.backend.entity.User;
import DA.backend.entity.UserEvaluate;
import DA.backend.projection.UserEvaluateSummaryProjection;
import DA.backend.service.UserEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userEvaluate")
@CrossOrigin
public class UserEvaluateController {

    @Autowired
    private UserEvaluateService userEvaluateService;
    @GetMapping("/allUserEvaluationsByEvaluateId")
    public ResponseEntity<List<UserEvaluateSummaryProjection>> getAllUserEvaluationsByEvaluateId(@RequestParam Long evaluateId) {
        List<UserEvaluateSummaryProjection> summaries = userEvaluateService.getAllUserEvaluationsByEvaluateId(evaluateId);
        return ResponseEntity.ok(summaries);
    }
    @PostMapping("/evaluate")
    public ResponseEntity<Void> evaluateUser(@RequestBody List<UserEvaluate> userEvaluates) {
        userEvaluateService.saveUserEvaluations(userEvaluates);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/calculateTotalScore")
    public ResponseEntity<Integer> calculateTotalScore(@RequestParam String userId, @RequestParam Long evaluateId) {
        int totalScore = userEvaluateService.calculateTotalScore(userId, evaluateId);
        return ResponseEntity.ok(totalScore);
    }

    @GetMapping("/calculateTotalScoreAdmin")
    public ResponseEntity<Integer> calculateTotalScoreAdmin(@RequestParam String userId, @RequestParam Long evaluateId) {
        int totalScoreAdmin = userEvaluateService.calculateTotalScoreAdmin(userId, evaluateId);
        return ResponseEntity.ok(totalScoreAdmin);
    }

    @GetMapping("/calculateTotalScoreManager")
    public ResponseEntity<Integer> calculateTotalScoreManager(@RequestParam String userId, @RequestParam Long evaluateId) {
        int totalScoreManager = userEvaluateService.calculateTotalScoreManager(userId, evaluateId);
        return ResponseEntity.ok(totalScoreManager);
    }

    @GetMapping("/userEvaluations")
    public ResponseEntity<List<UserEvaluateSummaryProjection>> getUserEvaluations(@RequestParam String userId) {
        List<UserEvaluateSummaryProjection> evaluations = userEvaluateService.getEvaluationsByUserId(userId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/evaluateByDepartment")
    public ResponseEntity<List<UserEvaluateSummaryProjection>> getEvaluateByDepartment(
            @RequestParam Long evaluateId,
            @RequestParam String userId) {
        List<UserEvaluateSummaryProjection> summaries = userEvaluateService.getEvaluationsByEvaluateAndDepartment(evaluateId, userId);
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/evaluateSummary")
    public ResponseEntity<List<UserEvaluateSummaryProjection>> getEvaluateSummary(@RequestParam Long evaluateId) {
        List<UserEvaluateSummaryProjection> summaries = userEvaluateService.getUserEvaluateSummaries(evaluateId);
        return ResponseEntity.ok(summaries);
    }
}
