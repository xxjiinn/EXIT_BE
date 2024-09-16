package club.pard.exit.ejection.repo;

import club.pard.exit.ejection.entity.Ejection;
import club.pard.exit.ejection.entity.EjectionRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface EjectionRepo extends JpaRepository<Ejection, Long> {
    public Long countByUserIdAndTimeOccurredGreaterThanEqual(Long userId, Timestamp targetTimestamp);

    @Query(value = "SELECT @rownum := @rownum + 1 AS rank, " +
            "e.user_id AS userId, " +
            "COUNT(e.user_id) AS ejectionCount " +
            "FROM ejection e, (SELECT @rownum := 0) r " +
            "WHERE e.time_occurred >= :timeCriteria " +
            "GROUP BY e.user_id " +
            "ORDER BY ejectionCount DESC", nativeQuery = true)
    List<EjectionRank> getRankList(Timestamp timeCriteria);
}

