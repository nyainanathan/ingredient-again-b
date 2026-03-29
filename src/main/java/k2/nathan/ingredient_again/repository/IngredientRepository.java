package k2.nathan.ingredient_again.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import k2.nathan.ingredient_again.entities.ingredient.CategoryEnum;
import k2.nathan.ingredient_again.entities.ingredient.Ingredient;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IngredientRepository {
    
    private final DataSource dataSource;

    /*
        GET /ingredients, qui retourne une liste d’objets JSON (relatifs aux
        ingrédients) et contenant les attributs : identifiant, nom, catégorie de
        l’ingrédient et prix. Les objets retournés doivent être ceux enregistrés dans la
        base de données.
    */

    public List<Ingredient> findAll(){


        List<Ingredient> ingredients = new ArrayList<>();

        String query = """
                SELECT
                    id, name, price, category
                FROM ingredient;
                """;
        
        try(
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Ingredient ingredient = new Ingredient();

                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));

                ingredients.add(ingredient);
                
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return ingredients;
    }
}
