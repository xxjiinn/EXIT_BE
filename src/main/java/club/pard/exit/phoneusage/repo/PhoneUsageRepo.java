package club.pard.exit.phoneusage.repo;

import club.pard.exit.phoneusage.entity.PhoneUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface PhoneUsageRepo extends JpaRepository<PhoneUsage, Long> {
    public Optional<PhoneUsage> findByUserIdAndDate(Long userId, Date date);
}