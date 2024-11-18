package DA.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserEvaluateId implements Serializable {

    @Column(name = "userId")
    private String userId;

    @Column(name = "evaluateId")
    private Long evaluateId;

    @Column(name = "questionId")
    private Long questionId;

    // Constructors
    public UserEvaluateId() {}

    public UserEvaluateId(String userId, Long evaluateId, Long questionId) {
        this.userId = userId;
        this.evaluateId = evaluateId;
        this.questionId = questionId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(Long evaluateId) {
        this.evaluateId = evaluateId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEvaluateId that = (UserEvaluateId) o;

        if (!Objects.equals(userId, that.userId)) return false;
        if (!Objects.equals(evaluateId, that.evaluateId)) return false;
        return Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, evaluateId, questionId);
    }
}
