package k2.nathan.ingredient_again.service;

import java.time.Instant;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import k2.nathan.ingredient_again.entities.ingredient.Ingredient;
import k2.nathan.ingredient_again.entities.ingredient.Unit;
import k2.nathan.ingredient_again.entities.stock.StockValue;
import k2.nathan.ingredient_again.errors.RessourceNotFoundException;
import k2.nathan.ingredient_again.repository.IngredientRepository;
import k2.nathan.ingredient_again.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
    
    private final StockMovementRepository stockMovementRepository;
    private final IngredientRepository ingredientRepository;

    public StockValue findIngredientStock(Integer id, Instant t, Unit unit) throws Exception{

        Ingredient ingredient = ingredientRepository.findById(id);

        if(ingredient.getId()==null){
            throw new RessourceNotFoundException("Ingredient.id={" + id+ "} is not found");
        }

        if(t == null || unit == null){
            throw new BadRequestException("Either mandatory query parameter 'at' or 'unit' is not provided");
        }

        return this.stockMovementRepository.findByIngredientId(id, t, unit);
    }
}
