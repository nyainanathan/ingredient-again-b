package k2.nathan.ingredient_again.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import k2.nathan.ingredient_again.entities.ingredient.Ingredient;
import k2.nathan.ingredient_again.errors.RessourceNotFoundException;
import k2.nathan.ingredient_again.service.DishService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes")
public class DishController {
    
    private final DishService dishService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Integer id){
        try {
             return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(this.dishService.findById(id));
        } catch (RessourceNotFoundException e){
            return ResponseEntity
                    .status(404)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
         catch (Exception e){
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAll(){
        try {
             return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(this.dishService.findAll());
        } catch (Exception e){
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> attachOrDetach(
        @PathVariable(name = "id") Integer id,
        @RequestBody List<Ingredient> ingredients
    ){
        try {
            return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(this.dishService.attachAndDetach(id, ingredients));
        } catch(RessourceNotFoundException e){
            return ResponseEntity
                .status(404)
                .header("Content-Type","text/plain")
                .body(e.getMessage());
        } catch(BadRequestException e){
            return ResponseEntity
                .status(400)
                .header("Content-Type","text/plain")
                .body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity
                .status(500)
                .header("Content-Type","text/plain")
                .body(e.getMessage());
        }
    }
}
