package fit.track.demo.service;

import fit.track.demo.model.Ingredient;
import fit.track.demo.model.User;
import fit.track.demo.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngredientServiceUnitTest {

    private IngredientRepository ingredientRepository;
    private IngredientService service;

    @BeforeEach
    void setUp() {
        ingredientRepository = mock(IngredientRepository.class);
        service = new IngredientService(ingredientRepository);
    }

    @Test
    void findAllUserIngredients_shouldReturnUserIngredients() {
        User user = new User();
        List<Ingredient> ingredients = List.of(new Ingredient());

        when(ingredientRepository.findByUser(user)).thenReturn(ingredients);

        List<Ingredient> result = service.findAllUserIngredients(user);

        assertEquals(ingredients, result);
        verify(ingredientRepository).findByUser(user);
    }

    // ---------- searchGlobal ----------

    @Test
    void searchGlobal_shouldReturnAllGlobal_whenQueryIsNull() {
        List<Ingredient> ingredients = List.of(new Ingredient());

        when(ingredientRepository.findByIsGlobalTrue()).thenReturn(ingredients);

        List<Ingredient> result = service.searchGlobal(null);

        assertEquals(ingredients, result);
        verify(ingredientRepository).findByIsGlobalTrue();
    }

    @Test
    void searchGlobal_shouldReturnAllGlobal_whenQueryIsBlank() {
        List<Ingredient> ingredients = List.of(new Ingredient());

        when(ingredientRepository.findByIsGlobalTrue()).thenReturn(ingredients);

        List<Ingredient> result = service.searchGlobal("   ");

        assertEquals(ingredients, result);
        verify(ingredientRepository).findByIsGlobalTrue();
    }

    @Test
    void searchGlobal_shouldSearchByName_whenQueryProvided() {
        List<Ingredient> ingredients = List.of(new Ingredient());

        when(ingredientRepository
                .findByIsGlobalTrueAndNameContainingIgnoreCase("rice"))
                .thenReturn(ingredients);

        List<Ingredient> result = service.searchGlobal("rice");

        assertEquals(ingredients, result);
        verify(ingredientRepository)
                .findByIsGlobalTrueAndNameContainingIgnoreCase("rice");
    }

    // ---------- searchUser ----------

    @Test
    void searchUser_shouldReturnAllUserIngredients_whenQueryIsNull() {
        User user = new User();
        List<Ingredient> ingredients = List.of(new Ingredient());

        when(ingredientRepository.findByUser(user)).thenReturn(ingredients);

        List<Ingredient> result = service.searchUser(user, null);

        assertEquals(ingredients, result);
        verify(ingredientRepository).findByUser(user);
    }

    @Test
    void searchUser_shouldSearchByName_whenQueryProvided() {
        User user = new User();
        List<Ingredient> ingredients = List.of(new Ingredient());

        when(ingredientRepository
                .findByUserAndNameContainingIgnoreCase(user, "milk"))
                .thenReturn(ingredients);

        List<Ingredient> result = service.searchUser(user, "milk");

        assertEquals(ingredients, result);
        verify(ingredientRepository)
                .findByUserAndNameContainingIgnoreCase(user, "milk");
    }

    // ---------- simple delegations ----------

    @Test
    void save_shouldDelegateToRepository() {
        Ingredient ingredient = new Ingredient();

        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);

        Ingredient result = service.save(ingredient);

        assertEquals(ingredient, result);
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void findById_shouldReturnIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);

        when(ingredientRepository.findById(1L))
                .thenReturn(Optional.of(ingredient));

        Optional<Ingredient> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(ingredient, result.get());
        verify(ingredientRepository).findById(1L);
    }

    @Test
    void deleteById_shouldCallRepository() {
        service.deleteById(1L);

        verify(ingredientRepository).deleteById(1L);
    }
}
