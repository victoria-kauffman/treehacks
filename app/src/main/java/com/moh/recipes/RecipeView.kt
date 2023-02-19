package com.moh.recipes

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RecipeView : AppCompatActivity() {

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((application as RecipeApplication).repository)
    }

    var recipeNameTv : EditText? = null
    var recipeTv : EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_view)

        val extra : Bundle? = getIntent().extras

        if (extra == null) {
            startActivity(Intent(applicationContext, SavedRecipes::class.java))
        }

        val recipeName = extra!!.getString("recipe_name")
        val recipeContents = extra!!.getString("recipe")
        val rid = extra!!.getInt("rid")

        recipeTv = findViewById(R.id.recipe_contents)
        recipeNameTv = findViewById(R.id.recipe_name)

        recipeNameTv!!.setText(recipeName)
        recipeTv!!.setText(recipeContents)

        val returnToSavedButton : Button = findViewById(R.id.return_to_saved)
        returnToSavedButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, SavedRecipes::class.java)) })

        val deleteButton : Button = findViewById(R.id.delete_recipe)
        deleteButton.setOnClickListener(View.OnClickListener {
            // Double check they actually want to do this, since it's irreversible
            val alert : AlertDialog.Builder = AlertDialog.Builder(this)

            alert.setTitle("Delete Recipe")
                 .setMessage("Are you sure you want to delete this recipe? This action is irreversible.")
                 .setPositiveButton(android.R.string.ok) { dialog, which ->
                    recipeViewModel.delete(rid)
                    startActivity(Intent(applicationContext, SavedRecipes::class.java))
                 }
                 .setNegativeButton(android.R.string.no) {dialog, which ->
                    dialog.cancel()
                 }
            alert.show()
        })

        val updateButton : Button = findViewById(R.id.update_recipe)
        updateButton.setOnClickListener(View.OnClickListener {
            // Double check they actually want to do this, since it's irreversible
            val alert : AlertDialog.Builder = AlertDialog.Builder(this)

            alert.setTitle("Edit Recipe")
                    .setMessage("Are you sure you want to edit this recipe? This action is irreversible.")
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        recipeViewModel.update(recipeNameTv!!.text.toString(), recipeTv!!.text.toString()!!, rid)
                    }
                    .setNegativeButton(android.R.string.no) {dialog, which ->
                        dialog.cancel()
                    }
            alert.show()
        })

    }
}