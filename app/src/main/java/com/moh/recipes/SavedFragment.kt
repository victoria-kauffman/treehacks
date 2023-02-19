package com.moh.recipes

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels

class SavedFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((requireActivity().application as RecipeApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe_list : LinearLayout = view.findViewById(R.id.recipe_list)

        // Show saved recipe names to start
        recipeViewModel.allRecipes.observe(requireActivity()) { recipeList ->
            recipeList?.let {
                val recipes: List<Recipe> = recipeList

                for (rec in recipes) {
                    val recButton = Button(requireContext())
                    recButton.text = rec.recipe_name
                    recipe_list.addView(recButton)
                    recButton.textSize = 30F

                    recButton.setOnClickListener(View.OnClickListener {
                        val i: Intent = Intent(requireContext().applicationContext, RecipeView::class.java)
                        i.putExtra("rid", rec.rid)
                        i.putExtra("recipe_name", rec.recipe_name)
                        i.putExtra("recipe", rec.recipe)
                        startActivity(i)
                    })
                }
            }
        }

        val backButton: Button = view.findViewById(R.id.back_to_chat)
        backButton.setOnClickListener(View.OnClickListener { startActivity(Intent(requireContext().applicationContext, MainActivity::class.java)) })

    }

}