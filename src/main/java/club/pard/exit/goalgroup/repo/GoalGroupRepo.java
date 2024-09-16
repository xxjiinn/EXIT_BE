package club.pard.exit.goalgroup.repo;

import club.pard.exit.goalgroup.entity.Goal;
import club.pard.exit.goalgroup.entity.GoalGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalGroupRepo extends JpaRepository<GoalGroup, Long> {
    public List<GoalGroup> findAllByUserId(Long userId);
}
