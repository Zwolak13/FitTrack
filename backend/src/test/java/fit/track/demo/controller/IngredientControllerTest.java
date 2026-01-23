package fit.track.demo.controller;

import fit.track.demo.model.Ingredient;
import fit.track.demo.model.User;
import fit.track.demo.service.IngredientService;
import fit.track.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngredientControllerUnitTest {

    private IngredientService ingredientService;
    private UserService userService;
    private IngredientController controller;

    @BeforeEach
    void setUp() {
        ingredientService = mock(IngredientService.class);
        userService = mock(UserService.class);
        controller = new IngredientController(ingredientService, userService);
    }

    @Test
    void getAll_shouldReturnGlobalIngredients() {
        List<Ingredient> ingredients = List.of(
                new Ingredient(),
                new Ingredient()
        );

        when(ingredientService.searchGlobal(null)).thenReturn(ingredients);

        ResponseEntity<List<Ingredient>> response = controller.getAll(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingredients, response.getBody());
        verify(ingredientService).searchGlobal(null);
    }

    @Test
    void getMine_shouldReturnUserIngredients() {
        User user = new User();
        user.setEmail("test@test.com");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        List<Ingredient> ingredients = List.of(new Ingredient());

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(ingredientService.searchUser(user, "q")).thenReturn(ingredients);

        ResponseEntity<List<Ingredient>> response =
                controller.getMine("q", auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingredients, response.getBody());
    }

    @Test
    void create_shouldAssignUserWhenIngredientIsNotGlobal() {
        User user = new User();
        user.setEmail("test@test.com");

        Ingredient ingredient = new Ingredient();
        ingredient.setGlobal(false);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(ingredientService.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<Ingredient> response =
                controller.create(ingredient, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody().getUser());
        verify(ingredientService).save(ingredient);
    }

    @Test
    void create_shouldNotAssignUserWhenIngredientIsGlobal() {
        Ingredient ingredient = new Ingredient();
        ingredient.setGlobal(true);

        when(ingredientService.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<Ingredient> response =
                controller.create(ingredient, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getUser());
        verify(userService, never()).findByEmail(any());
    }

    @Test
    void update_shouldUpdateAndReturnIngredient() {
        Ingredient existing = new Ingredient();
        existing.setId(1L);
        existing.setName("Old");

        Ingredient updated = new Ingredient();
        updated.setName("New");

        when(ingredientService.findById(1L)).thenReturn(Optional.of(existing));
        when(ingredientService.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<Ingredient> response =
                controller.update(1L, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New", response.getBody().getName());
    }

    @Test
    void update_shouldReturnNotFoundWhenMissing() {
        when(ingredientService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Ingredient> response =
                controller.update(1L, new Ingredient());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void delete_shouldReturnNoContent() {
        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ingredientService).deleteById(1L);
    }
}
