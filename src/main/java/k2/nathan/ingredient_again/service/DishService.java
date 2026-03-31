package k2.nathan.ingredient_again.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import k2.nathan.ingredient_again.entities.dish.Dish;
import k2.nathan.ingredient_again.entities.ingredient.Ingredient;
import k2.nathan.ingredient_again.errors.RessourceNotFoundException;
import k2.nathan.ingredient_again.repository.DishRepository;
import k2.nathan.ingredient_again.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishService {
    
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    public List<Dish> findAll(){
        return this.dishRepository.findAll();
    }

    public Dish findById(Integer dishId){
        Dish dish = this.dishRepository.findById(dishId);
        if (dish == null) {
            throw new RessourceNotFoundException("Dish.id={"+dishId+"} is not found");
        }
        return dish;
    }

    public List<Ingredient> findByNameAndPrice(Integer dishId, String ingredientName, Double price){

        //Throws an exception if the if dish is not found
        this.findById(dishId);

        List<Ingredient> ingredients = ingredientName != null ? 
        this.dishRepository.findDishIngredientByDishIdAndIngredientName(dishId, ingredientName)
        : this.dishRepository.findDishIngredientByDishId(dishId);

        return price != null ? ingredients.stream()
                                .filter(i -> Math.abs(i.getPrice() - price) <= 50).toList()
                            : ingredients ;
    }

    public Dish attachAndDetach(Integer dishId, List<Ingredient> ingredients) throws BadRequestException{

        if(ingredients.isEmpty()){
            throw new BadRequestException("You must have a request body");
        }

        Dish thisDish = this.dishRepository.findById(dishId);

        if(thisDish == null){
            throw new RessourceNotFoundException("Dish.id={" + dishId+ "} is not found");
        };

        for(Ingredient ing : ingredients){

            Ingredient isExisting = this.ingredientRepository.findById(ing.getId());
            
            if(isExisting.getId() != null) {
                if(thisDish.getIngredients().stream().map(Ingredient::getId).toList().contains(ing.getId())){
                    this.dishRepository.detachIngredient(dishId, ing.getId());
                } else {
                    this.dishRepository.attach(dishId, ing.getId());
                }

            }

        }

        return this.dishRepository.findById(dishId);
    }

}
