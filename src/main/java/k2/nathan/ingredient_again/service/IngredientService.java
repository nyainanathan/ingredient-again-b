package k2.nathan.ingredient_again.service;

import java.util.List;

import org.springframework.stereotype.Service;

import k2.nathan.ingredient_again.entities.ingredient.Ingredient;
import k2.nathan.ingredient_again.errors.RessourceNotFoundException;
import k2.nathan.ingredient_again.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientService {
    
    private final IngredientRepository ingredientRepository;

    public List<Ingredient> findAll(){
        return this.ingredientRepository.findAll();
    }

    public Ingredient findById(Integer id){

        Ingredient ingredient = this.ingredientRepository.findById(id);
        
        if(ingredient.getId() == null){
            throw new RessourceNotFoundException("Ingredient.id={" + id + "} not found");
        }

        return ingredient;
    }
}
