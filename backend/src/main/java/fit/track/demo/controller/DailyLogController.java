package fit.track.demo.controller;

import fit.track.demo.model.*;
import fit.track.demo.model.enums.MealType;
import fit.track.demo.repository.DailyLogRepository;
import fit.track.demo.repository.MealRepository;
import fit.track.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/daily-log")
@RequiredArgsConstructor
public class DailyLogController {

    private final DailyLogRepository dailyLogRepository;
    private final MealRepository mealRepository;
    private final UserService userService;

    @GetMapping("/{date}")
    public ResponseEntity<DailyLog> getDay(@PathVariable String date, Authentication auth) {
        User user = userService.findByEmail(auth.getName()).orElseThrow();
        LocalDate localDate = LocalDate.parse(date);

        DailyLog log = dailyLogRepository.findByUserAndDate(user, localDate)
                .orElseGet(() -> createEmptyDay(user, localDate));

        return ResponseEntity.ok(log);
    }

    private DailyLog createEmptyDay(User user, LocalDate date) {
        DailyLog log = new DailyLog();
        log.setUser(user);
        log.setDate(date);
        log.setTotalCalories(BigDecimal.ZERO);
        log.setTotalProtein(BigDecimal.ZERO);
        log.setTotalCarbs(BigDecimal.ZERO);
        log.setTotalFat(BigDecimal.ZERO);

        dailyLogRepository.save(log);

        for (MealType type : MealType.values()) {
            Meal meal = new Meal();
            meal.setType(type);
            meal.setDailyLog(log);
            meal.setCalories(BigDecimal.ZERO);
            meal.setProtein(BigDecimal.ZERO);
            meal.setCarbs(BigDecimal.ZERO);
            meal.setFat(BigDecimal.ZERO);
            mealRepository.save(meal);
            log.getMeals().add(meal);
        }

        return log;
    }

}
