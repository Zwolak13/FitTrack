package fit.track.demo.service;

import fit.track.demo.model.Ingredient;
import fit.track.demo.model.User;
import fit.track.demo.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public List<Ingredient> findAllUserIngredients(User user) {
        return ingredientRepository.findByUser(user);
    }

    public List<Ingredient> searchGlobal(String query) {
        if (query == null || query.isBlank()) return ingredientRepository.findByIsGlobalTrue();
        return ingredientRepository.findByIsGlobalTrueAndNameContainingIgnoreCase(query);
    }

    public List<Ingredient> searchUser(User user, String query) {
        if (query == null || query.isBlank()) return ingredientRepository.findByUser(user);
        return ingredientRepository.findByUserAndNameContainingIgnoreCase(user, query);
    }

    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Optional<Ingredient> findById(Long id) {
        return ingredientRepository.findById(id);
    }

    public void deleteById(Long id) {
        ingredientRepository.deleteById(id);
    }
}
