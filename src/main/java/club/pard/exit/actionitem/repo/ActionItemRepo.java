package club.pard.exit.actionitem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.pard.exit.actionitem.entity.ActionItem;

public interface ActionItemRepo extends JpaRepository<ActionItem, Long> {
    public boolean existsByUserIdAndCategoryAndContent(Long userId, String targetCategory, String targetContent);
    public List<ActionItem> findAllByUserIdOrderByIdAsc(Long userId);
    public ActionItem findFirst1ByUserIdOrderByExposureCountAsc(Long userId);
}

