package fit.track.demo.repository;

import fit.track.demo.model.DailyLog;
import fit.track.demo.model.Meal;
import fit.track.demo.model.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    Optional<Meal> findByDailyLogAndType(DailyLog dailyLog, MealType mealType);
}
