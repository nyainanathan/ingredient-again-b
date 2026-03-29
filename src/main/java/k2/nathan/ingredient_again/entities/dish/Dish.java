package k2.nathan.ingredient_again.entities.dish;

import java.util.List;

import k2.nathan.ingredient_again.entities.ingredient.Ingredient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;
}
