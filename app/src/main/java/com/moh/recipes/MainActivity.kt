package com.moh.recipes

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    var bottomNavigationView: BottomNavigationView? = null

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((application as RecipeApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment=HomeFragment()
        val savedFragment=SavedFragment()
        val shareFragment=ShareFragment()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    setCurrentFragment(homeFragment)
                    true
                }
                R.id.recipes-> {
                    setCurrentFragment(savedFragment)
                    true
                }
                R.id.shared-> {
                    setCurrentFragment(shareFragment)
                    true
                }
                else -> false
            }
        }
        setCurrentFragment(homeFragment)

    }

    fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}