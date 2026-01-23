package fit.track.demo.controller;

import fit.track.demo.auth.dto.CustomIngredientRequest;
import fit.track.demo.auth.dto.MealIngredientResponse;
import fit.track.demo.model.*;
import fit.track.demo.model.enums.MealType;
import fit.track.demo.repository.*;
import fit.track.demo.service.NutritionCalculatorService;
import fit.track.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealIngredientControllerUnitTest {

    private MealIngredientRepository mealIngredientRepository;
    private MealRepository mealRepository;
    private IngredientRepository ingredientRepository;
    private DailyLogRepository dailyLogRepository;
    private UserService userService;
    private NutritionCalculatorService nutritionCalculatorService;

    private MealIngredientController controller;

    @BeforeEach
    void setUp() {
        mealIngredientRepository = mock(MealIngredientRepository.class);
        mealRepository = mock(MealRepository.class);
        ingredientRepository = mock(IngredientRepository.class);
        dailyLogRepository = mock(DailyLogRepository.class);
        userService = mock(UserService.class);
        nutritionCalculatorService = mock(NutritionCalculatorService.class);

        controller = new MealIngredientController(
                mealIngredientRepository,
                mealRepository,
                ingredientRepository,
                dailyLogRepository,
                userService,
                nutritionCalculatorService
        );
    }

    @Test
    void addCustomIngredient_shouldCreateEverythingWhenMissing() {
        User user = new User();
        user.setEmail("test@test.com");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        CustomIngredientRequest request = new CustomIngredientRequest();
        request.setName("Custom");
        request.setCalories(BigDecimal.TEN);
        request.setProtein(BigDecimal.ONE);
        request.setCarbs(BigDecimal.ONE);
        request.setFat(BigDecimal.ONE);
        request.setMealType(MealType.BREAKFAST);
        request.setDate(LocalDate.of(2024, 1, 1));
        request.setAddToUserIngredients(true);

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(dailyLogRepository.findByUserAndDate(any(), any())).thenReturn(Optional.empty());
        when(mealRepository.findByDailyLogAndType(any(), any())).thenReturn(Optional.empty());

        when(dailyLogRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mealRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(ingredientRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mealIngredientRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<MealIngredientResponse> response =
                controller.addCustomIngredient(request, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(nutritionCalculatorService).recalculateMeal(any());
        verify(nutritionCalculatorService).recalculateDay(any());
    }

    @Test
    void deleteMealIngredient_shouldRemoveAndRecalculate() {
        User user = new User();
        user.setEmail("test@test.com");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        DailyLog dailyLog = new DailyLog();
        Meal meal = new Meal();
        meal.setDailyLog(dailyLog);

        MealIngredient mi = new MealIngredient();
        mi.setMeal(meal);

        meal.getIngredients().add(mi);

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(mealIngredientRepository.findById(1L)).thenReturn(Optional.of(mi));

        ResponseEntity<Void> response =
                controller.deleteMealIngredient(1L, auth);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(mealIngredientRepository).delete(mi);
        verify(nutritionCalculatorService).recalculateMeal(meal);
        verify(nutritionCalculatorService).recalculateDay(dailyLog);
        verify(mealRepository).save(meal);
    }

    @Test
    void addExistingIngredient_shouldAddIngredientToMeal() {
        User user = new User();
        user.setEmail("test@test.com");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        DailyLog dailyLog = new DailyLog();
        Meal meal = new Meal();
        meal.setDailyLog(dailyLog);

        Ingredient ingredient = new Ingredient();

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.of(ingredient));
        when(mealIngredientRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mealRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<MealIngredientResponse> response =
                controller.addExistingIngredient(
                        1L,
                        2L,
                        BigDecimal.valueOf(2),
                        auth
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(nutritionCalculatorService).recalculateMeal(meal);
        verify(nutritionCalculatorService).recalculateDay(dailyLog);
    }
}
