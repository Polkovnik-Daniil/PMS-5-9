package be.ehb.myrecipe.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(version = 1, entities = {Recipe.class}, exportSchema = false)
public abstract class RecipeDB extends RoomDatabase {
    public abstract RecipeDAO getRecipeDAO();
    private static RecipeDB instance;

    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public static RecipeDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, RecipeDB.class, "recipes.db").build();
        }
        return instance;
    }








}
