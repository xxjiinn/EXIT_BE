package club.pard.exit.goalgroup.repo;

import club.pard.exit.goalgroup.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepo extends JpaRepository<Goal, Long> {
    public List<Goal> findAllByUserIdAndGoalGroupId(Long userId, Long goalGroupId);
    public boolean existsByUserIdAndAppId(Long userId, String appId);
}
