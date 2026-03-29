package k2.nathan.ingredient_again.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import k2.nathan.ingredient_again.entities.ingredient.Ingredient;
import k2.nathan.ingredient_again.errors.RessourceNotFoundException;
import k2.nathan.ingredient_again.service.IngredientService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    
    private final IngredientService ingredientService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable(name = "id") Integer id){
        try {
            Ingredient ingredient = this.ingredientService.findById(id);
            return ResponseEntity
                    .status(200)
                    .header("Content-Type", "application/json")
                    .body(ingredient);
        } catch (RessourceNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAll(){
        try {
            List<Ingredient> ingredients = this.ingredientService.findAll();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(ingredients);
                    
        } catch (Exception e){
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "text/plain")
                .body(e.getMessage());
        }
    }

    
}
