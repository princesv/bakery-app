package com.prince.bakeryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

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

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListViewAdapter(this.getApplicationContext());
    }

    public class WidgetListViewAdapter implements RemoteViewsFactory {

        String gSearchResult;
        Context mContext;
        List<Double> quantity = new ArrayList<>();
        List<String> measure = new ArrayList<>();
        List<String> ingredient = new ArrayList<>();


        public WidgetListViewAdapter(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences sharedpreferences =
                    getSharedPreferences(RecipeActivity.SHARED_PREF, MODE_PRIVATE);
            String jsonRecipe = sharedpreferences.getString(RecipeActivity.TAG_INGREDIENTS_DATA, "");
            Ingredients ingredientsObject = getIngredientsFromJson(jsonRecipe);
            quantity = ingredientsObject.getQuantity();
            measure = ingredientsObject.getMeasure();
            ingredient = ingredientsObject.getIngredient();
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




        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredient.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),R.layout.widget_recipe_plate);
            views.setTextViewText(R.id.widget_ingredient_Title,ingredient.get(position));
            views.setTextViewText(R.id.widget_ingredient_measure,measure.get(position));
            views.setTextViewText(R.id.widget_ingredient_quantity, String.valueOf(quantity.get(position)));
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
