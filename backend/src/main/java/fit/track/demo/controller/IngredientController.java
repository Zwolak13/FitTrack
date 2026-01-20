package fit.track.demo.controller;

import fit.track.demo.model.Ingredient;
import fit.track.demo.model.User;
import fit.track.demo.service.IngredientService;
import fit.track.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll(
            @RequestParam(required = false) String q
    ) {
        List<Ingredient> ingredients = ingredientService.searchGlobal(q);
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Ingredient>> getMine(
            @RequestParam(required = false) String q,
            Authentication auth
    ) {
        User user = userService.findByEmail(auth.getName()).orElseThrow();
        List<Ingredient> ingredients = ingredientService.searchUser(user, q);
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping
    public ResponseEntity<Ingredient> create(@RequestBody Ingredient ingredient, Authentication auth) {
        if (!ingredient.isGlobal()) {
            User user = userService.findByEmail(auth.getName()).orElseThrow();
            ingredient.setUser(user);
        }
        return ResponseEntity.ok(ingredientService.save(ingredient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> update(@PathVariable Long id, @RequestBody Ingredient ingredient) {
        return ingredientService.findById(id)
                .map(existing -> {
                    existing.setName(ingredient.getName());
                    existing.setUnit(ingredient.getUnit());
                    existing.setCaloriesPer100g(ingredient.getCaloriesPer100g());
                    existing.setProteinPer100g(ingredient.getProteinPer100g());
                    existing.setCarbsPer100g(ingredient.getCarbsPer100g());
                    existing.setFatPer100g(ingredient.getFatPer100g());
                    existing.setGramsPerUnit(ingredient.getGramsPerUnit());
                    existing.setGlobal(ingredient.isGlobal());
                    return ResponseEntity.ok(ingredientService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ingredientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
