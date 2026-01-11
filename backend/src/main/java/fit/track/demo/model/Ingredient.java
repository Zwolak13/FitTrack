package fit.track.demo.model;

import jakarta.persistence.*;
import fit.track.demo.model.enums.Unit;

import java.math.BigDecimal;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;                     // GRAM, SZTUKA, ML, etc.

    public Ingredient() {
    }

    public Ingredient(String name, BigDecimal quantity, Unit unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    // Gettery i settery

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}