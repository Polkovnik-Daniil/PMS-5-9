package be.ehb.myrecipe.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDAO {

    @Insert
    void insertRecipe(Recipe recipe);

    @Update
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM Recipe ORDER BY recipe_name")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM Recipe WHERE recipe_name LIKE :name")
    LiveData<List<Recipe>> findRecipeByName(String name);


}
