package fit.track.demo.controller;

import fit.track.demo.model.DailyLog;
import fit.track.demo.model.Meal;
import fit.track.demo.model.User;
import fit.track.demo.model.enums.MealType;
import fit.track.demo.repository.DailyLogRepository;
import fit.track.demo.repository.MealRepository;
import fit.track.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DailyLogControllerUnitTest {

    private DailyLogRepository dailyLogRepository;
    private MealRepository mealRepository;
    private UserService userService;
    private DailyLogController controller;

    @BeforeEach
    void setUp() {
        dailyLogRepository = mock(DailyLogRepository.class);
        mealRepository = mock(MealRepository.class);
        userService = mock(UserService.class);
        controller = new DailyLogController(
                dailyLogRepository,
                mealRepository,
                userService
        );
    }

    @Test
    void getDay_shouldReturnExistingLog() {
        User user = new User();
        user.setEmail("test@test.com");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        DailyLog log = new DailyLog();
        log.setDate(LocalDate.of(2024, 1, 1));

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(dailyLogRepository.findByUserAndDate(user, log.getDate()))
                .thenReturn(Optional.of(log));

        ResponseEntity<DailyLog> response =
                controller.getDay("2024-01-01", auth);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(log, response.getBody());
        verify(mealRepository, never()).save(any());
    }

    @Test
    void getDay_shouldCreateEmptyLogWhenMissing() {
        User user = new User();
        user.setEmail("test@test.com");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(dailyLogRepository.findByUserAndDate(any(), any()))
                .thenReturn(Optional.empty());

        when(dailyLogRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mealRepository.save(any(Meal.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<DailyLog> response =
                controller.getDay("2024-01-02", auth);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(mealRepository, times(MealType.values().length)).save(any());
    }
}
