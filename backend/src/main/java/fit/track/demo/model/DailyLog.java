package fit.track.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "daily_logs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    private User user;

    @OneToMany(mappedBy = "dailyLog", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Meal> meals = new ArrayList<>();

    private BigDecimal totalCalories;
    private BigDecimal totalProtein;
    private BigDecimal totalCarbs;
    private BigDecimal totalFat;

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public User getUser() { return user; }
    public List<Meal> getMeals() { return meals; }
    public BigDecimal getTotalCalories() { return totalCalories; }
    public BigDecimal getTotalProtein() { return totalProtein; }
    public BigDecimal getTotalCarbs() { return totalCarbs; }
    public BigDecimal getTotalFat() { return totalFat; }

    public void setId(Long id) { this.id = id; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setUser(User user) { this.user = user; }
    public void setMeals(List<Meal> meals) { this.meals = meals; }
    public void setTotalCalories(BigDecimal totalCalories) { this.totalCalories = totalCalories; }
    public void setTotalProtein(BigDecimal totalProtein) { this.totalProtein = totalProtein; }
    public void setTotalCarbs(BigDecimal totalCarbs) { this.totalCarbs = totalCarbs; }
    public void setTotalFat(BigDecimal totalFat) { this.totalFat = totalFat; }
}
