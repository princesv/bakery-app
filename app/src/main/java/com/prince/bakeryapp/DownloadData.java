package com.prince.bakeryapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class DownloadData {



        // Create an ArrayList of mTeas
        static String gSearchResult;
       static final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
       static DelayerCallback gcallback;
    @Nullable static SimpleIdlingResource gIdlingResource;
        interface DelayerCallback{
            void onDone(String searchResult);
        }

        static void downloadData(Context context, final DelayerCallback callback,
                                  @Nullable final SimpleIdlingResource idlingResource) {

            /**
             * The IdlingResource is null in production as set by the @Nullable annotation which means
             * the value is allowed to be null.
             *
             * If the idle state is true, Espresso can perform the next action.
             * If the idle state is false, Espresso will wait until it is true before
             * performing the next action.
             */
            if (idlingResource != null) {
                idlingResource.setIdleState(false);
            }

            // Display a toast to let the user know the images are downloading
            gcallback = callback;
            gIdlingResource = idlingResource;
           /* String text = "Images are downloading";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            */
            new FetchDataFronInternet().execute(url);
           // callback.onDone(gSearchResult);


        }
       public static class FetchDataFronInternet extends AsyncTask<String,Void,String> {


           @Override
           protected String doInBackground(String... strings) {
               String stringUrl = strings[0];
               URL urlToFetchData = getUrlFromString(stringUrl);
               String searchResult = null;
               try {
                   searchResult = getResponseFromHttpUrl(urlToFetchData);
               } catch (IOException e) {
                   e.printStackTrace();
               }

               gSearchResult = searchResult;
               return searchResult;


           }

           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);
               gcallback.onDone(s);
           }
       }

    static URL getUrlFromString(String stringUrl){
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
    }

