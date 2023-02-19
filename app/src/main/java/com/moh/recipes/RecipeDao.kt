package com.moh.recipes

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("UPDATE recipes  SET recipe_name = :recipe_name, recipe = :recipe WHERE rid = :rid")
    suspend fun update(recipe_name : String, recipe : String, rid : Int)

    @Query("DELETE FROM recipes WHERE rid = :rid")
    suspend fun delete(rid : Int)

    @Insert
    suspend fun insert(recipe: Recipe)
}