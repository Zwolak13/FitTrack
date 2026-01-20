package fit.track.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fit.track.demo.model.enums.MealType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType type;

    @ManyToOne(optional = false)
    @JsonBackReference
    private DailyLog dailyLog;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealIngredient> ingredients = new ArrayList<>();

    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;

    public Long getId() { return id; }
    public MealType getType() { return type; }
    public DailyLog getDailyLog() { return dailyLog; }
    public List<MealIngredient> getIngredients() { return ingredients; }
    public BigDecimal getCalories() { return calories; }
    public BigDecimal getProtein() { return protein; }
    public BigDecimal getCarbs() { return carbs; }
    public BigDecimal getFat() { return fat; }

    public void setId(Long id) { this.id = id; }
    public void setType(MealType type) { this.type = type; }
    public void setDailyLog(DailyLog dailyLog) { this.dailyLog = dailyLog; }
    public void setIngredients(List<MealIngredient> ingredients) { this.ingredients = ingredients; }
    public void setCalories(BigDecimal calories) { this.calories = calories; }
    public void setProtein(BigDecimal protein) { this.protein = protein; }
    public void setCarbs(BigDecimal carbs) { this.carbs = carbs; }
    public void setFat(BigDecimal fat) { this.fat = fat; }
}
