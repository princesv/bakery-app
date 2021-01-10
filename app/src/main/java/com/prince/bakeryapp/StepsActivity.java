package com.prince.bakeryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends AppCompatActivity {
    static final String STEP_INDEX_EXTRA = "step-index";
    static final String STEPS_EXTRA = "steps-json";
    static final String FRAGMENT_TAG_STRING = "fragment-tag";
    String stepsJson;
    int index;
    Steps steps;
    TextView indexNumber;
    public static String STEPS_JSON_STATE ="steps-json";
    public static String INDEX_STATE = "index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        indexNumber = findViewById(R.id.index_number);
        if(savedInstanceState!=null){
            stepsJson = savedInstanceState.getString(STEPS_JSON_STATE);
            index = savedInstanceState.getInt(INDEX_STATE);
        }else {
            Intent intent = getIntent();
            stepsJson = intent.getStringExtra(STEPS_EXTRA);
            index = intent.getIntExtra(STEP_INDEX_EXTRA, 0);
        }
        indexNumber.setText(String.valueOf(index+1));
         steps = getStepsFromJson(stepsJson);
        StepFragment stepFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

             if(savedInstanceState == null){
                 stepFragment = new StepFragment();
                 stepFragment.setStepsAndIndex(stepsJson,index);
                 fragmentManager.beginTransaction().replace(R.id.step_detail_fragment_view,stepFragment, FRAGMENT_TAG_STRING).commit();
             }else {
                 stepFragment =(StepFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_STRING);
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STEPS_JSON_STATE,stepsJson);
        outState.putInt(INDEX_STATE,index);
    }

    public void nextStep(View view){
        if(index+1<steps.getDescriptions().size()){
            StepFragment stepFragment = new StepFragment();
        stepFragment.setStepsAndIndex(stepsJson,++index);
        indexNumber.setText(String.valueOf(index+1));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.step_detail_fragment_view,stepFragment).commit();}
        else{
            Toast.makeText(this, "task not possible", Toast.LENGTH_SHORT).show();
        }
    }
    public void prevStep(View view){
        if(index>0) {
            StepFragment stepFragment = new StepFragment();
            stepFragment.setStepsAndIndex(stepsJson, --index);
            indexNumber.setText(String.valueOf(index+1));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.step_detail_fragment_view, stepFragment).commit();
        }else{
            Toast.makeText(this, "task not possible", Toast.LENGTH_SHORT).show();
        }
    }
}
