package com.moh.recipes

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        private var INSTANCE: RecipeDatabase? = null
        fun getDbInstance(context: Context?): RecipeDatabase? {
            if (INSTANCE == null) {
                // Initialize database
                INSTANCE = databaseBuilder(context!!,
                        RecipeDatabase::class.java,
                        "recipes").build()
            }
            return INSTANCE
        }
    }
}