package com.moh.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    TextView responseTv,  questionTv;
    TextInputEditText queryTiet;

    Button saveButton, shareButton, restartButton;

    String url = "https://api.openai.com/v1/completions";
    String fullQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseTv = findViewById(R.id.idTVResponse);
        questionTv = findViewById(R.id.idTVQuestion);
        queryTiet = findViewById(R.id.idEdtQuery);

        saveButton = findViewById(R.id.saveRecipeButton);
        restartButton = findViewById(R.id.restartConvoButton);
        shareButton = findViewById(R.id.shareRecipeButton);

        restart();

        queryTiet.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_SEND) {
                responseTv.setText("Please wait...");

                restartButton.setVisibility(View.VISIBLE);

                getResponse(queryTiet.getText().toString());

                return true;
            }
            return false;
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });
    }

    private void getResponse(String query) {
        questionTv.setText(query);
        queryTiet.setText("");
        fullQuery += query + " ";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", fullQuery);
            jsonObject.put("temperature", 0);
            jsonObject.put("max_tokens", 100);
            jsonObject.put("top_p", 1);
            jsonObject.put("frequency_penalty", 0.0);
            jsonObject.put("presence_penalty", 0.0);

            makeRequest(jsonObject);
        } catch (JSONException e) {
            hideRecipeButtons();
            Log.e("Create JSON", "Unexpected JSON exception");
        }


    }

    private void makeRequest(JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            shareButton.setVisibility(View.VISIBLE);
                            saveButton.setVisibility(View.VISIBLE);

                            responseTv.setText(response.getJSONArray("choices").getJSONObject(0).getString("text"));
                        } catch (JSONException e) {
                            Log.e("Parse JSON", "Unexpected JSON exception");
                            hideRecipeButtons();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GTP Request", error.getMessage() + "\n" + error);
                        questionTv.setText("Question");
                        responseTv.setText("An error has occurred.");
                        hideRecipeButtons();
                    }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer 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");
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

    private void restart() {
        fullQuery = "Give me one recipe I could make, given the following: ";
        questionTv.setText("Start a conversation about what you want to make, what ingredients you have, etc. I'll recommend recipes accordingly.");
        responseTv.setText("");
        queryTiet.setText("");

        restartButton.setVisibility(View.INVISIBLE);
        hideRecipeButtons();
    }

    private void hideRecipeButtons() {
        saveButton.setVisibility(View.INVISIBLE);
        shareButton.setVisibility(View.INVISIBLE);
    }
}