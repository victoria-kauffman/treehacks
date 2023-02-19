package com.moh.recipes

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RecipeApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { RecipeDatabase.DatabaseBuilder.getDbInstance(this, applicationScope) }
    val repository by lazy { RecipeRepository(database.recipeDao()) }
}