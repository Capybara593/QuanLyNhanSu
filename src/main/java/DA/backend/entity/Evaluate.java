package DA.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Year;
import java.util.List;

@Entity
@Table(name = "evaluate")
public class Evaluate {
    public Evaluate(Long id, String name, Year year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public Evaluate() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Year year;
    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuestionSet getQuestionSets() {
        return questionSet;
    }

    public void setQuestionSets(QuestionSet questionSets) {
        this.questionSet = questionSets;
    }

    @ManyToOne
    @JoinColumn(name = "question_set_id")
    @JsonIgnore
    private QuestionSet questionSet;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @ManyToMany
    @JoinTable(
            name = "evaluate_user",
            joinColumns = @JoinColumn(name = "evaluate_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> users;
}
