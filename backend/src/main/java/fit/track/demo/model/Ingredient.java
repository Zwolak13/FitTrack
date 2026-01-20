package fit.track.demo.model;

import fit.track.demo.model.enums.Unit;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;
    @Column(nullable = false)
    private BigDecimal caloriesPer100g;
    @Column(nullable = false)
    private BigDecimal proteinPer100g;
    @Column(nullable = false)
    private BigDecimal carbsPer100g;
    @Column(nullable = false)
    private BigDecimal fatPer100g;
    @Column(nullable = false)
    private BigDecimal gramsPerUnit;
    @Column(nullable = false)
    private boolean isGlobal;
    @ManyToOne
    private User user;

    public Long getId() { return id; }
    public String getName() { return name; }
    public Unit getUnit() { return unit; }
    public BigDecimal getCaloriesPer100g() { return caloriesPer100g; }
    public BigDecimal getProteinPer100g() { return proteinPer100g; }
    public BigDecimal getCarbsPer100g() { return carbsPer100g; }
    public BigDecimal getFatPer100g() { return fatPer100g; }
    public BigDecimal getGramsPerUnit() { return gramsPerUnit; }
    public boolean isGlobal() { return isGlobal; }
    public User getUser() { return user; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public void setCaloriesPer100g(BigDecimal caloriesPer100g) { this.caloriesPer100g = caloriesPer100g; }
    public void setProteinPer100g(BigDecimal proteinPer100g) { this.proteinPer100g = proteinPer100g; }
    public void setCarbsPer100g(BigDecimal carbsPer100g) { this.carbsPer100g = carbsPer100g; }
    public void setFatPer100g(BigDecimal fatPer100g) { this.fatPer100g = fatPer100g; }
    public void setGramsPerUnit(BigDecimal gramsPerUnit) { this.gramsPerUnit = gramsPerUnit; }
    public void setGlobal(boolean global) { isGlobal = global; }
    public void setUser(User user) { this.user = user; }
}
