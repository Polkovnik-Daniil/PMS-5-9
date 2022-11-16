package be.ehb.myrecipe.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeDB recipeDB;
    private LiveData<List<Recipe>> recipeList;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        recipeDB = RecipeDB.getInstance(application);
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return recipeDB.getRecipeDAO().getAllRecipes();
    }


    public void insertRecipe(Recipe recipe){
        RecipeDB.databaseExecutor.execute(()->{
            recipeDB.getRecipeDAO().insertRecipe(recipe);
        });
    }

    public void updateRecipe(Recipe recipe){
        RecipeDB.databaseExecutor.execute(()->{
            recipeDB.getRecipeDAO().updateRecipe(recipe);
        });
    }

    public void deleteRecipe(Recipe recipe){
        RecipeDB.databaseExecutor.execute(()->{
            recipeDB.getRecipeDAO().deleteRecipe(recipe);
        });
    }




}
