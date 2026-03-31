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
    
    public Dish findById(Integer id) {
        Dish dish = null;

        String query = """
                        select dish.id as dish_id, dish.name as dish_name, dish_type, dish.price as dish_price
                        from dish
                        where dish.id = ?
                        """;

        try (
            Connection connection = dataSource.getConnection();
        ) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Dish theDish = new Dish();
                theDish.setId(resultSet.getInt("dish_id"));
                theDish.setName(resultSet.getString("dish_name"));
                theDish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));
                theDish.setPrice(resultSet.getObject("dish_price") == null
                        ? null : resultSet.getDouble("dish_price"));
                theDish.setIngredients(findDishIngredientByDishId(theDish.getId()));

                dish=theDish;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dish;
    }

    public List<Ingredient> findDishIngredientByDishId(Integer idDish) {
        String query =  """
                        SELECT i.id as ingredient_id,
                               i.name as ingredient_name,
                               i.price as ingredient_price,
                               i.category as ingredient_category
                        FROM ingredient i
                        JOIN dishingredients di ON i.id = di.id_ingredient
                        JOIN dish d ON d.id = di.id_dish
                        WHERE di.id_dish = ?
                        """;
        List<Ingredient> ingredients = new ArrayList<>();
        try(
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
        ) {

            ps.setInt(1, idDish);

            ResultSet rs =  ps.executeQuery();

            while(rs.next()) {
                    Ingredient ingre = new Ingredient(
                            rs.getInt("ingredient_id"),
                            rs.getString("ingredient_name"),
                            rs.getDouble("ingredient_price"),
                            CategoryEnum.valueOf(rs.getString("ingredient_category"))
                    );

                    ingredients.add(ingre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    public List<Ingredient> findDishIngredientByDishIdAndIngredientName(Integer idDish, String ingredientName) {
        String query =  """
                        SELECT i.id as ingredient_id,
                               i.name as ingredient_name,
                               i.price as ingredient_price,
                               i.category as ingredient_category
                        FROM ingredient i
                        JOIN dishingredients di ON i.id = di.id_ingredient
                        JOIN dish d ON d.id = di.id_dish
                        WHERE di.id_dish = ?
                        AND i.name ilike ?
                        """;
        List<Ingredient> ingredients = new ArrayList<>();
        try(
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
        ) {

            ps.setInt(1, idDish);
            ps.setString(2, "%"  + ingredientName + "%");

            ResultSet rs =  ps.executeQuery();

            while(rs.next()) {
                    Ingredient ingre = new Ingredient(
                            rs.getInt("ingredient_id"),
                            rs.getString("ingredient_name"),
                            rs.getDouble("ingredient_price"),
                            CategoryEnum.valueOf(rs.getString("ingredient_category"))
                    );

                    ingredients.add(ingre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }
    
    public void detachIngredient(Integer dishId, Integer ingredientId){
        String query = """
                DELETE FROM dishingredients 
                WHERE id_dish = ?
                AND id_ingredient = ?
                """;
        
        try(
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)
        ) {

            ps.setInt(1, dishId);
            ps.setInt(2, ingredientId);

            ps.execute();

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void attach(Integer dishId, Integer ingredientId){
        String query = """
                INSERT INTO dishingredients 
                (id_dish, id_ingredient, quantity_required, unit)
                VALUES (? , ?, ? , ?::unit_type)
                """;
        
        try(
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)
        ) {

            ps.setInt(1, dishId);
            ps.setInt(2, ingredientId);
            ps.setDouble(3, 1);
            ps.setString(4, Unit.KG.toString());

            ps.execute();

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


}
