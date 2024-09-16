package club.pard.exit.user.repo;

import club.pard.exit.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    public boolean existsByNickname(String targetNickname);
    public boolean existsByDeviceId(String targetDeviceId);

    public Optional<User> findByDeviceId(String targetDeviceId);
}
