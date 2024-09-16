package club.pard.exit.user.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import club.pard.exit.actionitem.entity.ActionItem;
import club.pard.exit.converter.PasswordEncryptConverter;
import club.pard.exit.actionitem.entity.ActionItem;
import club.pard.exit.ejection.entity.Ejection;
import club.pard.exit.goalgroup.entity.GoalGroup;
import club.pard.exit.phoneusage.entity.PhoneUsage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter
@ToString
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Convert(converter = PasswordEncryptConverter.class)
    @Column(unique = true, nullable = false)
    private String deviceId;

    @Setter
    Timestamp dateDeactivated;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalGroup> goalGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneUsage> phoneUsages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ejection> ejections = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActionItem> actionItems = new ArrayList<>();

    @Builder
    public User(String deviceId, String nickname)
    {
        this.deviceId = deviceId;
        this.nickname = nickname;
    }

    public void addGoalGroup(GoalGroup goalGroup){ this.goalGroups.add(goalGroup); goalGroup.setUser(this); }
    public void removeGoalGroup(GoalGroup goalGroup){ this.goalGroups.remove(goalGroup); goalGroup.setUser(null); }

    // public void addGoal(Goal goal){ this.goals.add(goal); goal.setUser(this); }

    public void addActionItem(ActionItem actionItem){
        this.actionItems.add(actionItem);
        actionItem.setUser(this);
    }

    public void removeActionItem(ActionItem actionItem){ this.actionItems.remove(actionItem); actionItem.setUser(null); }

    public void addEjection(Ejection ejection){ this.ejections.add(ejection); ejection.setUser(this); }
    public void removeEjection(Ejection ejection){ this.ejections.remove(ejection); ejection.setUser(null); }

    public void addPhoneUsage(PhoneUsage phoneUsage){ this.phoneUsages.add(phoneUsage); phoneUsage.setUser(this); }
    public void removePhoneUsage(PhoneUsage phoneUsage){ this.phoneUsages.remove(phoneUsage); phoneUsage.setUser(null); }
}

