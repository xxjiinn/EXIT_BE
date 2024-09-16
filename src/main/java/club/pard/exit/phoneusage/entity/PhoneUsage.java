package club.pard.exit.phoneusage.entity;

import java.sql.Date;

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
@Table(name = "phone_usage")
@Getter
@ToString
@NoArgsConstructor
public class PhoneUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id") @Setter
    private User user;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false) @Setter
    private Long timeUsed;

    @Builder
    public PhoneUsage(Date date, Long timeUsed){ this.date = date; this.timeUsed = timeUsed; }
}

