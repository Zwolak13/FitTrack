package fit.track.demo.service;

import fit.track.demo.model.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NutritionCalculatorService {

    public void recalculateMeal(Meal meal) {
        BigDecimal calories = BigDecimal.ZERO;
        BigDecimal protein = BigDecimal.ZERO;
        BigDecimal carbs = BigDecimal.ZERO;
        BigDecimal fat = BigDecimal.ZERO;

        for (MealIngredient mi : meal.getIngredients()) {
            Ingredient i = mi.getIngredient();

            if (i.isGlobal()) {
                BigDecimal grams = mi.getQuantity().multiply(i.getGramsPerUnit());
                calories = calories.add(calc(grams, i.getCaloriesPer100g(), true));
                protein = protein.add(calc(grams, i.getProteinPer100g(), true));
                carbs = carbs.add(calc(grams, i.getCarbsPer100g(), true));
                fat = fat.add(calc(grams, i.getFatPer100g(), true));
            } else {
                calories = calories.add(i.getCaloriesPer100g());
                protein = protein.add(i.getProteinPer100g());
                carbs = carbs.add(i.getCarbsPer100g());
                fat = fat.add(i.getFatPer100g());
            }
        }

        meal.setCalories(calories);
        meal.setProtein(protein);
        meal.setCarbs(carbs);
        meal.setFat(fat);
    }

    public void recalculateDay(DailyLog log) {
        BigDecimal calories = BigDecimal.ZERO;
        BigDecimal protein = BigDecimal.ZERO;
        BigDecimal carbs = BigDecimal.ZERO;
        BigDecimal fat = BigDecimal.ZERO;

        for (Meal meal : log.getMeals()) {
            calories = calories.add(meal.getCalories());
            protein = protein.add(meal.getProtein());
            carbs = carbs.add(meal.getCarbs());
            fat = fat.add(meal.getFat());
        }

        log.setTotalCalories(calories);
        log.setTotalProtein(protein);
        log.setTotalCarbs(carbs);
        log.setTotalFat(fat);
    }

    private BigDecimal calc(BigDecimal grams, BigDecimal per100, boolean isGlobal) {
        if (isGlobal) {

            return grams.multiply(per100).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        } else {
            return per100;
        }
    }

}
