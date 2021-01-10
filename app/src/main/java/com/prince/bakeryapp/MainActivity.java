package com.prince.bakeryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements RecepieListAdapter.ListItemClickListener {
    RecyclerView recipeCardList;
    TextView tvErrorMessage;
    ProgressBar progressBar;
    String gSearchResult;
    List<Integer> ids = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<String> ingredients = new ArrayList<>();
    List<String> steps = new ArrayList<>();
    List<Integer> servings = new ArrayList<>();
    static final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeCardList = findViewById(R.id.recipe_grid_recycler_view);
        tvErrorMessage = findViewById(R.id.errorMessage);
        progressBar = findViewById(R.id.progressBar);
        new FetchDataFronInternet().execute(url);
    }

    @Override
    public void onListItemClick(int listItemIndex) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.TAG_INGREDIENTS_DATA,ingredients.get(listItemIndex));
        intent.putExtra(RecipeActivity.TAG_STEPS_DATA,steps.get(listItemIndex));
        intent.putExtra(RecipeActivity.RECIPE_TITLE,names.get(listItemIndex));
        startActivity(intent);
        Toast.makeText(this, String.valueOf(listItemIndex), Toast.LENGTH_SHORT).show();
    }

    public class FetchDataFronInternet extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            tvErrorMessage.setVisibility(View.INVISIBLE);
            recipeCardList.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            URL urlToFetchData = getUrlFromString(stringUrl);
            String searchResult = null;
            try {
                searchResult = getResponseFromHttpUrl(urlToFetchData);
                gSearchResult = searchResult;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return searchResult;


        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(s);
            if(s!=null) {
                displayRecyclerView();
                RecipeList recipeList = getListDataForMainActivity(s);
                inflateMainActivity(recipeList);
            }else{
                displayErrorMessage();
            }
        }
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
        recipeCardList.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
    void displayRecyclerView(){
        recipeCardList.setVisibility(View.VISIBLE);
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
        RecepieListAdapter recepieListAdapter = new RecepieListAdapter(this,paramitersToInflateMoviesList,this);
       // GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recipeCardList.setLayoutManager(linearLayoutManager);
        recipeCardList.setHasFixedSize(true);
        recipeCardList.setAdapter(recepieListAdapter);
    }
}
