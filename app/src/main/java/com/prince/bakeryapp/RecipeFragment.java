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

import java.util.List;

public class RecipeFragment extends Fragment implements StepsListAdapter.ListItemClickListener{
     Ingredients ingredients;
     Steps steps;
     private ListItemClickListener mClickListener;


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




    public void setIngredients(Ingredients ingredients,Steps steps) {
        this.ingredients = ingredients;
        this.steps = steps;
    }

    // Mandatory empty constructor
    public RecipeFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

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


}
