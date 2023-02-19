package com.moh.recipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {
    var responseTv: TextView? = null
    var questionTv: TextView? = null
    var queryTiet: TextInputEditText? = null
    var saveButton: Button? = null
    var viewSavedButton: Button? = null
    var restartButton: Button? = null
    var url = "https://api.openai.com/v1/completions"
    var fullQuery: String = ""
    var recipeText: String = ""
    private val savedFragment=SavedFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        responseTv = view.findViewById(R.id.idTVResponse)
        questionTv = view.findViewById(R.id.idTVQuestion)
        queryTiet = view.findViewById(R.id.idEdtQuery)
        saveButton = view.findViewById(R.id.saveRecipeButton)
        viewSavedButton = view.findViewById(R.id.viewSavedRecipesButton)
        restartButton = view.findViewById(R.id.restartConvoButton)

        restart()
        queryTiet!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView: TextView?, id: Int, keyEvent: KeyEvent? ->
            if (id == EditorInfo.IME_ACTION_SEND) {
                responseTv!!.setText("Please wait...")
                restartButton!!.setVisibility(View.VISIBLE)
                getResponse(queryTiet!!.getText().toString())
                true
            }
            false
        })
        saveButton!!.setOnClickListener(View.OnClickListener { // Want to save the recipe to the Room Database

            val endOfIndex : Int = recipeText.substring(3).indexOf("\n") + 3

            recipeViewModel.insert( Recipe(recipeText.substring(0, endOfIndex).trim().replace("\n", ""),
                recipeText.substring(endOfIndex + 1)))
            Toast.makeText(requireActivity().applicationContext, "Recipe Saved.", Toast.LENGTH_SHORT).show()
        })
        viewSavedButton!!.setOnClickListener(View.OnClickListener { switchToFragment(savedFragment) })
        restartButton!!.setOnClickListener(View.OnClickListener { restart() })

    }


    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((requireActivity().application as RecipeApplication).repository)
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
            saveButton!!.visibility = View.INVISIBLE
            Log.e("Create JSON", "Unexpected JSON exception")
        }
    }

    private fun makeRequest(jsonObject: JSONObject) {
        val queue = Volley.newRequestQueue(this.context)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonObject,
            Response.Listener { response ->
                try {
                    saveButton!!.visibility = View.VISIBLE
                    recipeText = response.getJSONArray("choices").getJSONObject(0).getString("text")
                    responseTv!!.text = recipeText
                } catch (e: JSONException) {
                    Log.e("Parse JSON", "Unexpected JSON exception")
                    saveButton!!.visibility = View.INVISIBLE
                }
            }, Response.ErrorListener { error ->
                Log.e("GTP Request", """
     ${error.message}
     $error
     """.trimIndent())
                questionTv!!.text = ""
                responseTv!!.text = "An error has occurred."
                saveButton!!.visibility = View.INVISIBLE
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
        saveButton!!.visibility = View.INVISIBLE
    }

    private fun switchToFragment(fragment: Fragment) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.setCurrentFragment(fragment)
    }

}