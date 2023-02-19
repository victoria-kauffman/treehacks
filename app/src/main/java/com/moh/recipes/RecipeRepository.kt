package com.moh.recipes

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()
    val recipeNames : Flow<List<String>> = recipeDao.listRecipes()

    @WorkerThread
    fun getRecipe(rid : Int) : LiveData<List<Recipe>> {
        return recipeDao.getRecipe(rid)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }
}


