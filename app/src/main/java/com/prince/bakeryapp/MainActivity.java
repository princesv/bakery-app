package com.prince.bakeryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class MainActivity extends AppCompatActivity implements DownloadData.DelayerCallback {


    ListView mainRecipeList;
    TextView tvErrorMessage;
    ProgressBar progressBar;
    List<Integer> ids = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<String> ingredients = new ArrayList<>();
    List<String> steps = new ArrayList<>();
    List<Integer> servings = new ArrayList<>();
    static final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // recipeCardList = findViewById(R.id.recipe_grid_recycler_view);
        mainRecipeList = findViewById(R.id.recipe_grid_recycler_view);
        tvErrorMessage = findViewById(R.id.errorMessage);
        progressBar = findViewById(R.id.progressBar);
        getIdlingResource();
        initialiseViews();
        DownloadData.downloadData(this, this, mIdlingResource);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void initialiseViews(){
        progressBar.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.INVISIBLE);
        mainRecipeList.setVisibility(View.INVISIBLE);
    }





    RecipeList getListDataForMainActivity(String s){
        JSONArray recipeList = null;
        try {
            recipeList = new JSONArray(s);
            int size = recipeList.length();
            for(int i=0;i<size;i++){
                JSONObject obj = recipeList.getJSONObject(i);
                ids.add(obj.getInt("id"));
                names.add(obj.getString("name"));
                ingredients.add( obj.getJSONArray("ingredients").toString());
                steps.add(obj.getJSONArray("steps").toString());
                servings.add(obj.getInt("servings"));


            }
            RecipeList recipe = new RecipeList(ids,names,ingredients,steps,servings);
            return recipe;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }



    void displayErrorMessage(){
        mainRecipeList.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
    void displayListView(){
        mainRecipeList.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.INVISIBLE);
    }

    URL getUrlFromString(String stringUrl){
        try {
            URL urlToFetchData = new URL(stringUrl);
            return urlToFetchData;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    void inflateMainActivity(RecipeList paramitersToInflateMoviesList){
      //  RecepieListAdapter recepieListAdapter = new RecepieListAdapter(this,paramitersToInflateMoviesList,this);
       // GridLayoutManager layoutManager = new GridLayoutManager(this,2);
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

       // recipeCardList.setLayoutManager(linearLayoutManager);
       // recipeCardList.setHasFixedSize(true);
       // recipeCardList.setAdapter(recepieListAdapter);
        ListViewAdapter adapter = new ListViewAdapter(this, paramitersToInflateMoviesList);
        mainRecipeList.setAdapter(adapter);
        mainRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                intent.putExtra(RecipeActivity.TAG_INGREDIENTS_DATA,ingredients.get(position));
                intent.putExtra(RecipeActivity.TAG_STEPS_DATA,steps.get(position));
                intent.putExtra(RecipeActivity.RECIPE_TITLE,names.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDone(String searchResult) {
        progressBar.setVisibility(View.INVISIBLE);
        if(searchResult!=null) {
            displayListView();
            RecipeList recipeList = getListDataForMainActivity(searchResult);
            inflateMainActivity(recipeList);
        }else{
            displayErrorMessage();
        }
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }
}
