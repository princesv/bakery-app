package com.prince.bakeryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.ListItemClickListener {
    final static String TAG_INGREDIENTS_DATA = "ingredients-data";
    final static String TAG_STEPS_DATA = "steps-data";
    final static String RECIPE_TITLE = "recipe-title";
    String stepsJsonForStepsDetail;
    String ingredientsJsonForDetail;
    FragmentManager fragmentManager;
    RecipeFragment recipeFragment;
    Boolean FLAG_IS_TWO_PANE;
    int index;
    String recipeTitle;
    TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        titleView = findViewById(R.id.recipe_title);
        if(findViewById(R.id.small_screen_step_detail_fragment) != null){
            FLAG_IS_TWO_PANE = true;
            String ingredientsJson;
            String stepsJson;
            if (savedInstanceState == null) {
                ingredientsJson = getIntent().getStringExtra(TAG_INGREDIENTS_DATA);
                stepsJson = getIntent().getStringExtra(TAG_STEPS_DATA);

            } else {
                ingredientsJson = savedInstanceState.getString(TAG_INGREDIENTS_DATA);
                stepsJson = savedInstanceState.getString(TAG_STEPS_DATA);
            }

            Ingredients ingredients = getIngredientsFromJson(ingredientsJson);
            Steps steps = getStepsFromJson(stepsJson);
            ingredientsJsonForDetail = ingredientsJson;
            stepsJsonForStepsDetail = stepsJson;
            recipeFragment = new RecipeFragment();
            recipeFragment.setIngredients(ingredientsJson, stepsJson);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.small_screen_Steps_list_fragment, recipeFragment).commit();


            if(savedInstanceState!=null){
                index = savedInstanceState.getInt(StepsActivity.INDEX_STATE);
            }else {
                Intent intent = getIntent();
               // stepsJson = intent.getStringExtra(STEPS_EXTRA);
                index = intent.getIntExtra(StepsActivity.STEP_INDEX_EXTRA, 0);
            }

            steps = getStepsFromJson(stepsJson);
            StepFragment stepFragment;
            FragmentManager fragmentManager = getSupportFragmentManager();

            if(savedInstanceState == null){
                stepFragment = new StepFragment();
                stepFragment.setStepsAndIndex(stepsJson,index);
                fragmentManager.beginTransaction().replace(R.id.small_screen_step_detail_fragment,stepFragment, StepsActivity.FRAGMENT_TAG_STRING).commit();
            }else {
                stepFragment =(StepFragment) getSupportFragmentManager().findFragmentByTag(StepsActivity.FRAGMENT_TAG_STRING);
            }
        }else {
            FLAG_IS_TWO_PANE = false;
            String ingredientsJson;
            String stepsJson;
            if (savedInstanceState == null) {
                ingredientsJson = getIntent().getStringExtra(TAG_INGREDIENTS_DATA);
                stepsJson = getIntent().getStringExtra(TAG_STEPS_DATA);
                recipeTitle = getIntent().getStringExtra(RECIPE_TITLE);
            } else {
                ingredientsJson = savedInstanceState.getString(TAG_INGREDIENTS_DATA);
                stepsJson = savedInstanceState.getString(TAG_STEPS_DATA);
                recipeTitle = savedInstanceState.getString(RECIPE_TITLE);
                recipeTitle = savedInstanceState.getString(RECIPE_TITLE);
            }
            titleView.setText(recipeTitle);
            Ingredients ingredients = getIngredientsFromJson(ingredientsJson);
            Steps steps = getStepsFromJson(stepsJson);
            ingredientsJsonForDetail = ingredientsJson;
            stepsJsonForStepsDetail = stepsJson;
            recipeFragment = new RecipeFragment();
            recipeFragment.setIngredients(ingredientsJson, stepsJson);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_view, recipeFragment).commit();


        }


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
        if(FLAG_IS_TWO_PANE){
            StepFragment stepFragment;
            FragmentManager fragmentManager = getSupportFragmentManager();


                stepFragment = new StepFragment();
                stepFragment.setStepsAndIndex(stepsJsonForStepsDetail,index);
                fragmentManager.beginTransaction().replace(R.id.small_screen_step_detail_fragment,stepFragment, StepsActivity.FRAGMENT_TAG_STRING).commit();

        }else {
            Intent intent = new Intent(RecipeActivity.this, StepsActivity.class);
            intent.putExtra(StepsActivity.STEPS_EXTRA, stepsJsonForStepsDetail);
            intent.putExtra(StepsActivity.STEP_INDEX_EXTRA, listItemIndex);
            startActivity(intent);
        }

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_INGREDIENTS_DATA,ingredientsJsonForDetail);
        outState.putString(TAG_STEPS_DATA,stepsJsonForStepsDetail);
        outState.putString(RECIPE_TITLE,recipeTitle);
        if(FLAG_IS_TWO_PANE){
            outState.putInt(StepsActivity.INDEX_STATE,index);
        }
    }
}
