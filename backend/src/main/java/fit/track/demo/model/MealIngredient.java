package fit.track.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "meal_ingredients")
public class MealIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JsonBackReference
    private Meal meal;
    @ManyToOne(optional = false)
    private Ingredient ingredient;
    @Column(nullable = false)
    private BigDecimal quantity;

    public Long getId() { return id; }
    public Meal getMeal() { return meal; }
    public Ingredient getIngredient() { return ingredient; }
    public BigDecimal getQuantity() { return quantity; }
    public void setId(Long id) { this.id = id; }
    public void setMeal(Meal meal) { this.meal = meal; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}
