package club.pard.exit.goalgroup.entity;

import club.pard.exit.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "goal_group")
@Getter
@ToString
@NoArgsConstructor
public class GoalGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @Setter @Column(nullable = false)
    private String title;

    // // TODO: revise GoalGroup so that apps list could be added
    // @Setter @OneToMany(mappedBy = "goalGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Goal> goals = new ArrayList<>();

    @Setter @Column(nullable = false)
    private Long timeBudget;

    @Setter @Column(nullable = false)
    private Long nudgeInterval;


    @Builder
    public GoalGroup(String title, Long timeBudget, Long nudgeInterval)
    {
        this.title = title;
        this.timeBudget = timeBudget;
        this.nudgeInterval = nudgeInterval;
    }

    // // TODO: revise GoalGroup so that apps list could be added
    // public void addGoal(Goal goal)
    // {
    //     this.goals.add(goal);
    //     goal.setUser(this.getUser());
    //     goal.setGoalGroup(this);
    //     goal.setTimeBudget(this.timeBudget);
    //     goal.setNudgeInterval(this.nudgeInterval);
    // }
    //
    // public void removeGoal(Goal goal)
    // {
    //     this.goals.remove(goal);
    //     goal.setUser(null);
    //     goal.setGoalGroup(null);
    // }
}
