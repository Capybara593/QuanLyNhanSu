package DA.backend.service;

import DA.backend.entity.*;
import DA.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluateService {
    @Autowired
    EvaluateRepository evaluateRepository;
    @Autowired
    QuestionSetRepository questionSetRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;
    public boolean add(Evaluate evaluate){
        try {
            evaluateRepository.save(evaluate);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
    public boolean update(Evaluate evaluate){
        Optional<Evaluate>  optionalEvaluate = evaluateRepository.findById(evaluate.getId());
        if (optionalEvaluate.isPresent()){
            Evaluate evaluate1 = optionalEvaluate.get();
            evaluate1.setName(evaluate.getName());
            evaluate1.setYear(evaluate.getYear());
            evaluate1.setQuestionSets(evaluate.getQuestionSets());
            evaluateRepository.save(evaluate1);
            return true;
        }
        return false;
    }
    public boolean addQuestionSet(Long evaluateId, Long questionSetId){
        Optional<Evaluate> optionalEvaluate = evaluateRepository.findById(evaluateId);
        Optional<QuestionSet> optionalQuestionSet = questionSetRepository.findById(questionSetId);
        if(optionalQuestionSet.isPresent() && optionalEvaluate.isPresent()){
            Evaluate evaluate = optionalEvaluate.get();
            QuestionSet questionSet = optionalQuestionSet.get();
            evaluate.setQuestionSets(questionSet);
            evaluateRepository.save(evaluate);
            return true;
        }
        return false;
    }
    public List<Evaluate> list(){
        return evaluateRepository.findAll();
    }
    public List<Question> listQuestion(Long evaluateId){
        Optional<Evaluate> optionalEvaluate = evaluateRepository.findById(evaluateId);
        if(optionalEvaluate.isPresent()){
            Evaluate evaluate = optionalEvaluate.get();
            return evaluate.getQuestionSets().getQuestions();
        }
        return  null;
    }


}
