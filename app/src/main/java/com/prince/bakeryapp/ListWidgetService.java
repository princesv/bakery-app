package com.prince.bakeryapp;

import android.content.Context;
import android.content.Intent;
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

    public static class WidgetListViewAdapter implements RemoteViewsFactory {

        String gSearchResult;
        Context mContext;
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> ingredients = new ArrayList<>();
        List<String> steps = new ArrayList<>();
        List<Integer> servings = new ArrayList<>();

        public WidgetListViewAdapter(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            try {
                gSearchResult = getResponseFromHttpUrl(getUrlFromString(MainActivity.url));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(gSearchResult != null){
                JSONArray recipeList = null;
                    try {
                        recipeList = new JSONArray(gSearchResult);
                        int size = recipeList.length();
                        for(int i=0;i<size;i++){
                            JSONObject obj = recipeList.getJSONObject(i);
                            ids.add(obj.getInt("id"));
                            names.add(obj.getString("name"));
                            ingredients.add( obj.getJSONArray("ingredients").toString());
                            steps.add(obj.getJSONArray("steps").toString());
                            servings.add(obj.getInt("servings"));


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

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



        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ids.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),R.layout.widget_recipe_plate);
            views.setTextViewText(R.id.widget_recepie_view,names.get(position));
            views.setTextViewText(R.id.widget_servings_view,String.valueOf(servings.get(position)));
            Intent intent = new Intent();
            intent.putExtra(RecipeActivity.TAG_STEPS_DATA,steps.get(position));
            intent.putExtra(RecipeActivity.TAG_INGREDIENTS_DATA,ingredients.get(position));
            views.setOnClickFillInIntent(R.id.widget_recepie_view,intent);
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
