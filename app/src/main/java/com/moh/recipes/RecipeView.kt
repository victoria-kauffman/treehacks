package com.moh.recipes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class RecipeView : AppCompatActivity() {

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((application as RecipeApplication).repository)
    }

    var recipeNameTv : EditText? = null
    var recipeTv : EditText? = null
    var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_view)

        val extra : Bundle? = getIntent().extras
        val homeFragment=HomeFragment()
        val savedFragment=SavedFragment()
        val shareFragment=ShareFragment()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    switchToFragment(homeFragment)
                    true
                }
                R.id.recipes-> {
                    switchToFragment(savedFragment)
                    true
                }
                R.id.shared-> {
                    switchToFragment(shareFragment)
                    true
                }
                else -> false
            }
        }

        val recipeName = extra!!.getString("recipe_name")
        val recipeContents = extra!!.getString("recipe")
        val rid = extra!!.getInt("rid")

        recipeTv = findViewById(R.id.recipe_contents)
        recipeNameTv = findViewById(R.id.recipe_name)

        recipeNameTv!!.setText(recipeName)
        recipeTv!!.setText(recipeContents)

        val returnToSavedButton : Button = findViewById(R.id.return_to_saved)
        returnToSavedButton.setOnClickListener(View.OnClickListener {switchToFragment(savedFragment)
             })

        val deleteButton : Button = findViewById(R.id.delete_recipe)
        deleteButton.setOnClickListener(View.OnClickListener {
            // Double check they actually want to do this, since it's irreversible
            val alert : AlertDialog.Builder = AlertDialog.Builder(this)

            alert.setTitle("Delete Recipe")
                 .setMessage("Are you sure you want to delete this recipe? This action is irreversible.")
                 .setPositiveButton(android.R.string.ok) { dialog, which ->
                    recipeViewModel.delete(rid)
                     switchToFragment(savedFragment)
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

    private fun switchToFragment(fragment: Fragment) {
        // Create an instance of the new Fragment you want to move to
        // Use the FragmentManager to replace the current Fragment with the new Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container,fragment)
            .commit()
    }

}