package k2.nathan.ingredient_again.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import k2.nathan.ingredient_again.entities.dish.Dish;
import k2.nathan.ingredient_again.entities.dish.DishTypeEnum;
import k2.nathan.ingredient_again.entities.ingredient.CategoryEnum;
import k2.nathan.ingredient_again.entities.ingredient.DishIngredient;
import k2.nathan.ingredient_again.entities.ingredient.Ingredient;
import k2.nathan.ingredient_again.entities.ingredient.Unit;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DishRepository {
    
    private final DataSource dataSource;

    public List<Dish> findAll() {

        List<Dish> dishes = new ArrayList<>();
        String query = """
                        select dish.id as dish_id, dish.name as dish_name, dish_type, dish.price as dish_price
                        from dish
                        """;

        try (
            Connection connection = dataSource.getConnection();
        ) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("dish_id"));
                dish.setName(resultSet.getString("dish_name"));
                dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));
                dish.setPrice(resultSet.getObject("dish_price") == null
                        ? null : resultSet.getDouble("dish_price"));
                dish.setIngredients(findDishIngredientByDishId(dish.getId()));

                dishes.add(dish);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishes;
    }

    private List<DishIngredient> findDishIngredientByDishId(Integer idDish) {
        String query =  """
                        SELECT i.id as ingredient_id,
                               i.name as ingredient_name,
                               i.price as ingredient_price,
                               i.category as ingredient_category,
                               di.id as dish_ingredient_id,
                               di.quantity_required as quantity_required,
                               di.unit as  ingredient_unit,
                               d.id as dish_id,
                               d.name as dish_name,
                               d.dish_type as dish_type,
                               d.price as dish_price
                        FROM ingredient i
                        JOIN dishingredients di ON i.id = di.id_ingredient
                        JOIN dish d ON d.id = di.id_dish
                        WHERE di.id_dish = ?
                        """;
        List<DishIngredient> ingredients = new ArrayList<>();
        try(
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
        ) {


            ps.setInt(1, idDish);

            ResultSet rs =  ps.executeQuery();

            while(rs.next()) {
                DishIngredient ingredient = new DishIngredient();
                ingredient.setId(rs.getInt("dish_ingredient_id"));
                // ingredient.setDish(
                //         new Dish(
                //             rs.getInt("dish_id"),
                //                 rs.getString("dish_name"),
                //                 DishTypeEnum.valueOf(rs.getString("dish_type")),
                //                 rs.getDouble("dish_price"),
                //                 List.of()
                //         )
                // );
                ingredient.setIngredient(
                        new Ingredient(
                                rs.getInt("ingredient_id"),
                                rs.getString("ingredient_name"),
                                rs.getDouble("ingredient_price"),
                                CategoryEnum.valueOf(rs.getString("ingredient_category"))
                        )
                );
                ingredient.setUnit(Unit.valueOf(rs.getString("ingredient_unit")));
                ingredient.setQuantity(rs.getDouble("quantity_required"));
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }
}
