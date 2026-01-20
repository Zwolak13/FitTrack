package fit.track.demo.repository;

import fit.track.demo.model.DailyLog;
import fit.track.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    Optional<DailyLog> findByUserAndDate(User user, LocalDate date);
}
