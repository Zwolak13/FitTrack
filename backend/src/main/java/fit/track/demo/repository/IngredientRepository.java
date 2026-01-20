package fit.track.demo.repository;

import fit.track.demo.model.Ingredient;
import fit.track.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByIsGlobalTrue();
    List<Ingredient> findByIsGlobalTrueAndNameContainingIgnoreCase(String name);
    List<Ingredient> findByUser(User user);
    List<Ingredient> findByUserAndNameContainingIgnoreCase(User user, String name);
}

