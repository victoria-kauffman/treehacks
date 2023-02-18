package com.moh.recipes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Recipe (
    @PrimaryKey(autoGenerate = true)
    val rid: Int,
    val recipe_name: String?,
    val recipe: String?
)