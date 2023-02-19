package com.moh.recipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData

internal class SavedRecipes : AppCompatActivity() {

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((application as RecipeApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_recipes)

        val recipe_list : LinearLayout = findViewById(R.id.recipe_list)

        // Show saved recipe names to start
        recipeViewModel.allRecipes.observe(this) { recipeList ->
            recipeList?.let {
                val recipes: List<Recipe> = recipeList

                for (rec in recipes) {
                    val recButton = Button(this)
                    recButton.text = rec.recipe_name
                    recipe_list.addView(recButton)

                    recButton.setOnClickListener(View.OnClickListener {
                        val i: Intent = Intent(applicationContext, RecipeView::class.java)
                        i.putExtra("rid", rec.rid)
                        i.putExtra("recipe_name", rec.recipe_name)
                        i.putExtra("recipe", rec.recipe)
                        startActivity(i)
                    })
                }
            }
        }

        val backButton: Button = findViewById(R.id.back_to_chat)
        backButton.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, MainActivity::class.java)) })


        }
    }