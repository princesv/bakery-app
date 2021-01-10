package com.prince.bakeryapp;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment implements StepsListAdapter.ListItemClickListener{
     String ingredientsJson;
     String  stepsJson;
     Ingredients ingredients;
     Steps steps;
     private ListItemClickListener mClickListener;
    final static String TAG_INGREDIENTS_DATA = "ingredients-data";
    final static String TAG_STEPS_DATA = "steps-data";


    public interface ListItemClickListener{
        void onListItemClick(int listItemIndex);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mClickListener = (RecipeFragment.ListItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public void onListItemClick(int listItemIndex) {
       // Toast.makeText(getContext(),String.valueOf(listItemIndex), Toast.LENGTH_SHORT).show();
        mClickListener.onListItemClick(listItemIndex);

    }




    public void setIngredients(String ingredients,String steps) {
        this.ingredientsJson = ingredients;
        this.stepsJson = steps;
    }

    // Mandatory empty constructor
    public RecipeFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        if(savedInstanceState != null) {
            if (ingredientsJson == null && stepsJson == null) {
                ingredientsJson = savedInstanceState.getString(TAG_INGREDIENTS_DATA) ;
                stepsJson = savedInstanceState.getString(TAG_STEPS_DATA);
            }
        }
        ingredients = getIngredientsFromJson(ingredientsJson);
        steps = getStepsFromJson(stepsJson);

        // Get a reference to the GridView in the fragment_master_list xml layout file
        RecyclerView ingredientsListView = rootView.findViewById(R.id.ingredients_recycler_view);
        RecyclerView stepsListView = rootView.findViewById(R.id.steps_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ingredientsListView.setLayoutManager(layoutManager);
        IngredientsListAdapter adapter = new IngredientsListAdapter(getContext(),ingredients);
        ingredientsListView.setAdapter(adapter);
        StepsListAdapter stepsListAdapter = new StepsListAdapter(getContext(),steps.getShortDescriptions(),this);
        LinearLayoutManager stepsListLayoutManager = new LinearLayoutManager(getContext());
        stepsListView.setLayoutManager(stepsListLayoutManager);
        stepsListView.setAdapter(stepsListAdapter);
        // Return the root view
        return rootView;
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_INGREDIENTS_DATA,ingredientsJson);
        outState.putString(TAG_STEPS_DATA,stepsJson);
    }
}
