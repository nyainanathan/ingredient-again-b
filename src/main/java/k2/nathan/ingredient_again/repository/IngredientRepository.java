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

    /*
        GET /ingredients/{id} qui retourne un objet JSON relatif à l’ingrédient
        portant l’identifiant fourni à travers {id} et contenant les attributs : identifiant,
        nom, catégorie de l’ingrédient et prix. Dans le cas où l’ingrédient n’est pas
        trouvé, alors retourner une réponse HTTP contenant le code de statut 404 et
        un message textuel disant que “Ingredient.id={id) is not found”.
    */
   public Ingredient findById(Integer id){
        String query = """
                SELECT
                    id, name, price, category
                FROM ingredient
                WHERE id = ?
                """;
        Ingredient ingredient = new Ingredient();

        try(
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
            }
            

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ingredient;
   }
}
