package com.moh.recipes

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    val allRecipes : LiveData<List<Recipe>> = repository.allRecipes.asLiveData()
    val recipeNames : LiveData<List<String>> = repository.recipeNames.asLiveData()

    fun getRecipe(rid : Int) : Recipe? {
        val recipes : List<Recipe>? = repository.getRecipe(rid).value;

        if (recipes != null && !recipes.isEmpty()) {
            return recipes!![0]
        }
        return null
    }

    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }

    fun delete(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }

    fun update(recipe: Recipe) = viewModelScope.launch {
        repository.update(recipe)
    }
}

class WordViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}