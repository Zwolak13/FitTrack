package fit.track.demo.controller;

import fit.track.demo.auth.dto.CustomIngredientRequest;
import fit.track.demo.auth.dto.MealIngredientResponse;
import fit.track.demo.model.*;
import fit.track.demo.model.enums.Unit;
import fit.track.demo.repository.*;
import fit.track.demo.service.NutritionCalculatorService;
import fit.track.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/meal-ingredients")
@RequiredArgsConstructor
public class MealIngredientController {

    private final MealIngredientRepository mealIngredientRepository;
    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final DailyLogRepository dailyLogRepository;
    private final UserService userService;
    private final NutritionCalculatorService nutritionCalculatorService;

    @PostMapping("/add-custom")
    public ResponseEntity<MealIngredientResponse> addCustomIngredient(
            @RequestBody CustomIngredientRequest request,
            Authentication auth
    ) {
        User user = userService.findByEmail(auth.getName()).orElseThrow();

        DailyLog dailyLog = dailyLogRepository.findByUserAndDate(user, request.getDate())
                .orElseGet(() -> {
                    DailyLog dl = new DailyLog();
                    dl.setUser(user);
                    dl.setDate(request.getDate());
                    return dailyLogRepository.save(dl);
                });

        Meal meal = mealRepository.findByDailyLogAndType(dailyLog, request.getMealType())
                .orElseGet(() -> {
                    Meal m = new Meal();
                    m.setDailyLog(dailyLog);
                    m.setType(request.getMealType());
                    return mealRepository.save(m);
                });

        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.getName());
        ingredient.setCaloriesPer100g(request.getCalories());
        ingredient.setProteinPer100g(request.getProtein());
        ingredient.setCarbsPer100g(request.getCarbs());
        ingredient.setFatPer100g(request.getFat());
        ingredient.setGramsPerUnit(BigDecimal.ONE);
        ingredient.setUnit(Unit.GRAM);
        if (request.isAddToUserIngredients()) ingredient.setUser(user);

        ingredientRepository.save(ingredient);

        MealIngredient mi = new MealIngredient();
        mi.setMeal(meal);
        mi.setIngredient(ingredient);
        mi.setQuantity(BigDecimal.ONE);

        mealIngredientRepository.save(mi);

        meal.getIngredients().add(mi);
        nutritionCalculatorService.recalculateMeal(meal);
        nutritionCalculatorService.recalculateDay(dailyLog);
        mealRepository.save(meal);

        return ResponseEntity.ok(new MealIngredientResponse(mi));
    }

    @DeleteMapping("/{mealIngredientId}")
    public ResponseEntity<Void> deleteMealIngredient(
            @PathVariable Long mealIngredientId,
            Authentication auth
    ) {
        User user = userService.findByEmail(auth.getName()).orElseThrow();
        MealIngredient mi = mealIngredientRepository.findById(mealIngredientId)
                .orElseThrow(() -> new RuntimeException("MealIngredient not found"));

        DailyLog dailyLog = mi.getMeal().getDailyLog();
        Meal meal = mi.getMeal();

        meal.getIngredients().remove(mi);
        mealIngredientRepository.delete(mi);

        nutritionCalculatorService.recalculateMeal(meal);
        nutritionCalculatorService.recalculateDay(dailyLog);
        mealRepository.save(meal);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<MealIngredientResponse> addExistingIngredient(
            @RequestParam Long mealId,
            @RequestParam Long ingredientId,
            @RequestParam BigDecimal quantity,
            Authentication auth
    ) {
        User user = userService.findByEmail(auth.getName()).orElseThrow();

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        DailyLog dailyLog = meal.getDailyLog();

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        MealIngredient mi = new MealIngredient();
        mi.setMeal(meal);
        mi.setIngredient(ingredient);
        mi.setQuantity(quantity);

        mealIngredientRepository.save(mi);

        meal.getIngredients().add(mi);
        nutritionCalculatorService.recalculateMeal(meal);
        nutritionCalculatorService.recalculateDay(dailyLog);
        mealRepository.save(meal);

        return ResponseEntity.ok(new MealIngredientResponse(mi));
    }



}
