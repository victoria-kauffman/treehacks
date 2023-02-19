package com.moh.recipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var responseTv: TextView? = null
    var questionTv: TextView? = null
    var queryTiet: TextInputEditText? = null
    var saveButton: Button? = null
    var viewSavedButton: Button? = null
    var restartButton: Button? = null
    var url = "https://api.openai.com/v1/completions"
    var fullQuery: String = ""
    var recipeText: String = ""
    var recipeViewModel: RecipeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        responseTv = findViewById(R.id.idTVResponse)
        questionTv = findViewById(R.id.idTVQuestion)
        queryTiet = findViewById(R.id.idEdtQuery)
        saveButton = findViewById(R.id.saveRecipeButton)
        viewSavedButton = findViewById(R.id.viewSavedRecipesButton)
        restartButton = findViewById(R.id.restartConvoButton)

        restart()
        queryTiet!!.setOnEditorActionListener(OnEditorActionListener { textView: TextView?, id: Int, keyEvent: KeyEvent? ->
            if (id == EditorInfo.IME_ACTION_SEND) {
                responseTv!!.setText("Please wait...")
                restartButton!!.setVisibility(View.VISIBLE)
                getResponse(queryTiet!!.getText().toString())
                true
            }
            false
        })
        saveButton!!.setOnClickListener(View.OnClickListener { // Want to save the recipe to the Room Database

            val endOfIndex = recipeText.indexOf('\n')

            recipeViewModel = ViewModelProvider(this@MainActivity).get(RecipeViewModel::class.java)
            recipeViewModel!!.insert( Recipe(recipeText.substring(0, endOfIndex),
                                            recipeText.substring(endOfIndex + 1)))
            Toast.makeText(this@MainActivity, "Recipe Saved.", Toast.LENGTH_SHORT).show()
        })
        viewSavedButton!!.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, SavedRecipes::class.java)) })
        restartButton!!.setOnClickListener(View.OnClickListener { restart() })
    }

    private fun getResponse(query: String) {
        questionTv!!.text = query
        queryTiet!!.setText("")
        fullQuery += "$query "
        try {
            val jsonObject = JSONObject()
            jsonObject.put("model", "text-davinci-003")
            jsonObject.put("prompt", fullQuery)
            jsonObject.put("temperature", 0)
            jsonObject.put("max_tokens", 1024)
            jsonObject.put("top_p", 1)
            jsonObject.put("frequency_penalty", 0.0)
            jsonObject.put("presence_penalty", 0.0)
            makeRequest(jsonObject)
        } catch (e: JSONException) {
            hideRecipeButtons()
            Log.e("Create JSON", "Unexpected JSON exception")
        }
    }

    private fun makeRequest(jsonObject: JSONObject) {
        val queue = Volley.newRequestQueue(applicationContext)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonObject,
                Response.Listener { response ->
                    try {
                        viewSavedButton!!.visibility = View.VISIBLE
                        saveButton!!.visibility = View.VISIBLE
                        recipeText = response.getJSONArray("choices").getJSONObject(0).getString("text")
                        responseTv!!.text = recipeText
                    } catch (e: JSONException) {
                        Log.e("Parse JSON", "Unexpected JSON exception")
                        hideRecipeButtons()
                    }
                }, Response.ErrorListener { error ->
            Log.e("GTP Request", """
     ${error.message}
     $error
     """.trimIndent())
            questionTv!!.text = ""
            responseTv!!.text = "An error has occurred."
            hideRecipeButtons()
        }) {
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer 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"
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(jsonObjectRequest)
    }

    private fun restart() {
        fullQuery = "Give me one recipe I could make, given the following. Make sure the name of the dish is alone on the first line and the ingredients and directions are beneath. "
        questionTv!!.text = "Start a conversation about what you want to make, what ingredients you have, etc. I'll recommend recipes accordingly."
        responseTv!!.text = ""
        queryTiet!!.setText("")
        restartButton!!.visibility = View.INVISIBLE
        hideRecipeButtons()
    }

    private fun hideRecipeButtons() {
        saveButton!!.visibility = View.INVISIBLE
        viewSavedButton!!.visibility = View.INVISIBLE
    }
}