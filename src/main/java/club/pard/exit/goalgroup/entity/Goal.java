package club.pard.exit.goalgroup.entity;

import club.pard.exit.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goal")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @Setter @ManyToOne @JoinColumn(name = "goal_group_id")
    private GoalGroup goalGroup;

    @Column(nullable = false)
    private String appId;

    @Setter @Column(nullable = false)
    private Long timeBudget;

    @Setter @Column(nullable = false)
    private Long nudgeInterval;

    @Builder
    public Goal(String appId, Long timeBudget, Long nudgeInterval)
    {
        this.appId = appId;
        this.timeBudget = timeBudget;
        this.nudgeInterval = nudgeInterval;
    }
}
