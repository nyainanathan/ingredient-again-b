package k2.nathan.ingredient_again.entities.stock;

import k2.nathan.ingredient_again.entities.ingredient.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockValue {
    private Double quantity;
    private Unit unit;
}
