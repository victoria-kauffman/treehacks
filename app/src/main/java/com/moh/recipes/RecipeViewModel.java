package com.moh.recipes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    RecipeDatabase db;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        db = RecipeDatabase.getDbInstance(application.getApplicationContext());
    }

    public LiveData<List<String>> listRecipes() {
        return db.recipeDao().getRecipes();
    }

    public void insert(Recipe recipe) {
        db.recipeDao().insert(recipe);
    }
}
