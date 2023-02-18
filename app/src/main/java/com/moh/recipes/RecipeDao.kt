package com.moh.recipes

import androidx.room.*

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe)

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<Recipe>

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT recipe_name FROM recipes")
    suspend fun listRecipes(): List<String>

    @Query("SELECT * FROM recipes WHERE rid = :rid")
    suspend fun getRecipe(rid: Int): Recipe

    @Query("SELECT * FROM recipes")
    suspend fun findRecipes(): List<Recipe>
}