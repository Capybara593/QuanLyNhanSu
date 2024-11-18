package DA.backend.controller;

import DA.backend.entity.Question;
import DA.backend.entity.QuestionSet;
import DA.backend.service.QuestionSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionSet")
public class QuestionSetController {

    @Autowired
    private QuestionSetService questionSetService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addQuestionSet(@RequestBody QuestionSet questionSet) {
        return ResponseEntity.ok(questionSetService.add(questionSet));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateQuestionSet(@RequestBody QuestionSet questionSet) {
        return ResponseEntity.ok(questionSetService.update(questionSet));
    }

    @PostMapping("/addQuestions")
    public ResponseEntity<Boolean> addQuestions(@RequestParam Long id, @RequestBody List<Question> questions) {
        return ResponseEntity.ok(questionSetService.addQuestion(id, questions));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteQuestionSet(@RequestParam Long id) {
        return ResponseEntity.ok(questionSetService.delete(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<QuestionSet>> list() {
        return ResponseEntity.ok(questionSetService.list());
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> listQuestionsByQuestionSet(@RequestParam Long id) {
        return ResponseEntity.ok(questionSetService.listQuestionByQuestionSet(id));
    }

    @DeleteMapping("/deleteQuestion")
    public ResponseEntity<Boolean> deleteQuestionByQuestionSet(@RequestParam Long questionSetId, @RequestParam Long questionId) {
        return ResponseEntity.ok(questionSetService.deleteQuestionByQuestionSet(questionSetId, questionId));
    }
}
