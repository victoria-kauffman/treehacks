package com.moh.recipes

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(recipe_name : String, recipe : String, rid : Int) {
        recipeDao.update(recipe_name, recipe, rid)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(rid: Int) {
        recipeDao.delete(rid)
    }
}


