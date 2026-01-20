package fit.track.demo.auth.dto;

import fit.track.demo.model.enums.MealType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomIngredientRequest {
    private String name;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;
    private MealType mealType;
    private LocalDate date;
    private boolean addToUserIngredients = true;
}
