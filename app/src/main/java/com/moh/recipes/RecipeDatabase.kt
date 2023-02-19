package com.moh.recipes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    object DatabaseBuilder {
        private var INSTANCE: RecipeDatabase? = null
        fun getDbInstance(context: Context, scope: CoroutineScope): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RecipeDatabase::class.java,
                        "recipes").build()
                INSTANCE!!
            }
        }
    }
}