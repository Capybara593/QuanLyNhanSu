package DA.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_evaluate")
public class UserEvaluate {

    @EmbeddedId
    private UserEvaluateId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    @ManyToOne
    @MapsId("evaluateId")
    @JoinColumn(name = "evaluateId")
    @JsonIgnore
    private Evaluate evaluate;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "questionId")
    @JsonIgnore
    private Question question;

    // Cột bổ sung (nếu cần)
    @Column(name = "sore")
    private int  score;

    // Constructors
    public UserEvaluate() {}


    // Getters and Setters
    public UserEvaluateId getId() {
        return id;
    }

    public void setId(UserEvaluateId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Evaluate getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Evaluate evaluate) {
        this.evaluate = evaluate;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UserEvaluate(UserEvaluateId id, User user, Evaluate evaluate, Question question, int score) {
        this.id = id;
        this.user = user;
        this.evaluate = evaluate;
        this.question = question;
        this.score = score;
    }

    // equals() and hashCode() dựa trên 'id'
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEvaluate that = (UserEvaluate) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
