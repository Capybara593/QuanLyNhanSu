package DA.backend.service;

import DA.backend.entity.Question;
import DA.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;

    public boolean add(Question question) {
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Question question) {
        try {
            Optional<Question> optionalQuestion = questionRepository.findById(question.getId());
            if(optionalQuestion.isPresent()){
                Question question1 = optionalQuestion.get();
                question1.setQuestion(question.getQuestion());
                question1.setCore(question1.getCore());
                questionRepository.save(question1);
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    public List<Question> questionList(){
        return questionRepository.findAll();
    }
}
