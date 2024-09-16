package club.pard.exit.actionitem.entity;

import club.pard.exit.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "action_item")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActionItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @Setter @Column(nullable = false)
    private String category;

    @Setter @Column(nullable = false)
    private String content;

    @Setter
    private Long exposureCount;

    @Builder
    public ActionItem(String category, String content)
    {
        this.category = category;
        this.content = content;
        this.exposureCount = 0L;
    }
}

