package fit.track.demo.service;

import fit.track.demo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NutritionCalculatorServiceUnitTest {

    private NutritionCalculatorService service;

    @BeforeEach
    void setUp() {
        service = new NutritionCalculatorService();
    }

    @Test
    void recalculateMeal_shouldCalculateValuesForGlobalIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setGlobal(true);
        ingredient.setCaloriesPer100g(new BigDecimal("200"));
        ingredient.setProteinPer100g(new BigDecimal("10"));
        ingredient.setCarbsPer100g(new BigDecimal("20"));
        ingredient.setFatPer100g(new BigDecimal("5"));
        ingredient.setGramsPerUnit(new BigDecimal("50"));

        MealIngredient mi = new MealIngredient();
        mi.setIngredient(ingredient);
        mi.setQuantity(new BigDecimal("2")); // 2 * 50 = 100g

        Meal meal = new Meal();
        meal.setIngredients(List.of(mi));

        service.recalculateMeal(meal);

        assertEquals(new BigDecimal("200.000000"), meal.getCalories());
        assertEquals(new BigDecimal("10.000000"), meal.getProtein());
        assertEquals(new BigDecimal("20.000000"), meal.getCarbs());
        assertEquals(new BigDecimal("5.000000"), meal.getFat());
    }

    @Test
    void recalculateMeal_shouldUseDirectValuesForNonGlobalIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setGlobal(false);
        ingredient.setCaloriesPer100g(new BigDecimal("150"));
        ingredient.setProteinPer100g(new BigDecimal("12"));
        ingredient.setCarbsPer100g(new BigDecimal("30"));
        ingredient.setFatPer100g(new BigDecimal("8"));

        MealIngredient mi = new MealIngredient();
        mi.setIngredient(ingredient);
        mi.setQuantity(BigDecimal.ONE);

        Meal meal = new Meal();
        meal.setIngredients(List.of(mi));

        service.recalculateMeal(meal);

        assertEquals(new BigDecimal("150"), meal.getCalories());
        assertEquals(new BigDecimal("12"), meal.getProtein());
        assertEquals(new BigDecimal("30"), meal.getCarbs());
        assertEquals(new BigDecimal("8"), meal.getFat());
    }

    @Test
    void recalculateMeal_shouldSumMultipleIngredients() {
        Ingredient i1 = new Ingredient();
        i1.setGlobal(false);
        i1.setCaloriesPer100g(new BigDecimal("100"));
        i1.setProteinPer100g(new BigDecimal("5"));
        i1.setCarbsPer100g(new BigDecimal("10"));
        i1.setFatPer100g(new BigDecimal("2"));

        Ingredient i2 = new Ingredient();
        i2.setGlobal(false);
        i2.setCaloriesPer100g(new BigDecimal("200"));
        i2.setProteinPer100g(new BigDecimal("15"));
        i2.setCarbsPer100g(new BigDecimal("30"));
        i2.setFatPer100g(new BigDecimal("6"));

        MealIngredient mi1 = new MealIngredient();
        mi1.setIngredient(i1);

        MealIngredient mi2 = new MealIngredient();
        mi2.setIngredient(i2);

        Meal meal = new Meal();
        meal.setIngredients(List.of(mi1, mi2));

        service.recalculateMeal(meal);

        assertEquals(new BigDecimal("300"), meal.getCalories());
        assertEquals(new BigDecimal("20"), meal.getProtein());
        assertEquals(new BigDecimal("40"), meal.getCarbs());
        assertEquals(new BigDecimal("8"), meal.getFat());
    }

    @Test
    void recalculateDay_shouldSumMealsValues() {
        Meal m1 = new Meal();
        m1.setCalories(new BigDecimal("500"));
        m1.setProtein(new BigDecimal("30"));
        m1.setCarbs(new BigDecimal("50"));
        m1.setFat(new BigDecimal("20"));

        Meal m2 = new Meal();
        m2.setCalories(new BigDecimal("300"));
        m2.setProtein(new BigDecimal("20"));
        m2.setCarbs(new BigDecimal("40"));
        m2.setFat(new BigDecimal("10"));

        DailyLog log = new DailyLog();
        log.setMeals(List.of(m1, m2));

        service.recalculateDay(log);

        assertEquals(new BigDecimal("800"), log.getTotalCalories());
        assertEquals(new BigDecimal("50"), log.getTotalProtein());
        assertEquals(new BigDecimal("90"), log.getTotalCarbs());
        assertEquals(new BigDecimal("30"), log.getTotalFat());
    }
}
