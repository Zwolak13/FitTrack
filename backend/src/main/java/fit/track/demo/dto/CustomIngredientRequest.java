package fit.track.demo.dto;

import java.math.BigDecimal;

public class CustomIngredientRequest {
    private String name;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;

    public String getName() { return name; }
    public BigDecimal getCalories() { return calories; }
    public BigDecimal getProtein() { return protein; }
    public BigDecimal getCarbs() { return carbs; }
    public BigDecimal getFat() { return fat; }
    public void setName(String name) { this.name = name; }
    public void setCalories(BigDecimal calories) { this.calories = calories; }
    public void setProtein(BigDecimal protein) { this.protein = protein; }
    public void setCarbs(BigDecimal carbs) { this.carbs = carbs; }
    public void setFat(BigDecimal fat) { this.fat = fat; }
}
