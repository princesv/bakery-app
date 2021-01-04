package com.prince.bakeryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.ListItemClickListener {
    final static String TAG_INGREDIENTS_DATA = "ingredients-data";
    final static String TAG_STEPS_DATA = "steps-data";
    String stepsJsonForStepsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        String ingredientsJson = getIntent().getStringExtra(TAG_INGREDIENTS_DATA);
        String stepsJson = getIntent().getStringExtra(TAG_STEPS_DATA);
        Ingredients ingredients = getIngredientsFromJson(ingredientsJson);
        Steps steps = getStepsFromJson(stepsJson);
        stepsJsonForStepsDetail = stepsJson;
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setIngredients(ingredients,steps);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_view,recipeFragment).commit();


    }
    Ingredients getIngredientsFromJson(String json){
        try {
            List<Double> quantity = new ArrayList<>();
            List<String> measure = new ArrayList<>();
            List<String> ingredient = new ArrayList<>();
            JSONArray ingredients = new JSONArray(json);
            for(int i=0;i<ingredients.length();i++){
                JSONObject obj = ingredients.getJSONObject(i);
                quantity.add(obj.getDouble("quantity"));
                measure.add(obj.getString("measure"));
                ingredient.add(obj.getString("ingredient"));

            }
            Ingredients ingredients1 = new Ingredients(quantity,measure,ingredient);
            return ingredients1;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    Steps getStepsFromJson(String json){
        try {
            List<Integer> ids = new ArrayList<>();
            List<String> shortDescriptions = new ArrayList<>();
            List<String> descriptions = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            JSONArray stepsArray = new JSONArray(json);
            for(int i=0;i<stepsArray.length();i++){
                JSONObject obj = stepsArray.getJSONObject(i);
                ids.add(obj.getInt("id"));
                shortDescriptions.add(obj.getString("shortDescription"));
                descriptions.add(obj.getString("description"));
                urls.add(obj.getString("videoURL"));

            }
            Steps steps = new Steps(ids,shortDescriptions,descriptions,urls);
            return steps;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onListItemClick(int listItemIndex) {
        Intent intent = new Intent(RecipeActivity.this,StepsActivity.class);
        intent.putExtra(StepsActivity.STEPS_EXTRA,stepsJsonForStepsDetail);
        intent.putExtra(StepsActivity.STEP_INDEX_EXTRA,listItemIndex);
        startActivity(intent);

    }
}
