package DA.backend.controller;

import DA.backend.entity.Evaluate;
import DA.backend.entity.Question;
import DA.backend.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluate")
@CrossOrigin
public class EvaluateController {

    @Autowired
    private EvaluateService evaluateService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addEvaluate(@RequestBody Evaluate evaluate) {
        return ResponseEntity.ok(evaluateService.add(evaluate));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateEvaluate(@RequestBody Evaluate evaluate) {
        return ResponseEntity.ok(evaluateService.update(evaluate));
    }

    @PostMapping("/addQuestionSet")
    public ResponseEntity<Boolean> addQuestionSet(@RequestParam Long evaluateId, @RequestParam Long questionSetId) {
        return ResponseEntity.ok(evaluateService.addQuestionSet(evaluateId, questionSetId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Evaluate>> listEvaluates() {
        return ResponseEntity.ok(evaluateService.list());
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> listQuestionsByEvaluate(@RequestParam Long evaluateId) {
        return ResponseEntity.ok(evaluateService.listQuestion(evaluateId));
    }
}
