package fit.track.demo.auth.dto;

import fit.track.demo.model.MealIngredient;

import java.math.BigDecimal;

public class MealIngredientResponse {
    private Long id;
    private String ingredientName;
    private BigDecimal quantity;

    public MealIngredientResponse(MealIngredient mi) {
        this.id = mi.getId();
        this.ingredientName = mi.getIngredient().getName();
        this.quantity = mi.getQuantity();
    }

    public Long getId() { return id; }
    public String getIngredientName() { return ingredientName; }
    public BigDecimal getQuantity() { return quantity; }
}