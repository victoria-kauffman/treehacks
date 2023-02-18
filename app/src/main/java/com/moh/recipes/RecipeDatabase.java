package com.moh.recipes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();

    private static RecipeDatabase INSTANCE;

    public static RecipeDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {
            // Initialize database
            INSTANCE = Room.databaseBuilder(context,
                    RecipeDatabase.class,
                    "recipes").build();
        }

        return INSTANCE;
    }
}
