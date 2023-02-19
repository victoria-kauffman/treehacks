package com.moh.recipes

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT recipe_name FROM recipes")
    fun listRecipes(): Flow<List<String>>

    @Query("SELECT * FROM recipes WHERE rid = :rid")
    fun getRecipe(rid: Int): LiveData<List<Recipe>>

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Insert
    suspend fun insert(recipe: Recipe)
}