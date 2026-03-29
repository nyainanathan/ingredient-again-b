package k2.nathan.ingredient_again.entities.ingredient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ingredient {
    private Integer id;
    private String name;
    private Double price;
    private CategoryEnum category;
}
