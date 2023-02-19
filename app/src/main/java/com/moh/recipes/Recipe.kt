package com.moh.recipes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe (
    var recipe_name: String,
    var recipe: String
) {
    @PrimaryKey(autoGenerate = true)
    var rid: Int = 0
}