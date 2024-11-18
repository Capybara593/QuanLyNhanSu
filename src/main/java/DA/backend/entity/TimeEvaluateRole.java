package DA.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "timeEvaluateRole")
public class TimeEvaluateRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startDay;
    private Date endDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public Role getRoles() {
        return role;
    }

    public void setRoles(Role roles) {
        this.role = roles;
    }

    public Evaluate getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Evaluate evaluate) {
        this.evaluate = evaluate;
    }

    public TimeEvaluateRole() {
    }

    public TimeEvaluateRole(Long id, Date startDay, Date endDay) {
        this.id = id;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "roleId")
    @JsonIgnore
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @ManyToOne
    @JoinColumn(name = "evaluateId")
    @JsonIgnore
    private Evaluate evaluate;
}
