package com.prince.bakeryapp;

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
    String stepsJson;
    int index;
    Steps steps;
    TextView indexNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Intent intent = getIntent();
        indexNumber = findViewById(R.id.index_number);
         stepsJson = intent.getStringExtra(STEPS_EXTRA);
         index = intent.getIntExtra(STEP_INDEX_EXTRA,0);
         steps = getStepsFromJson(stepsJson);
        StepFragment stepFragment = new StepFragment();
        stepFragment.setStepsAndIndex(steps,index);
        indexNumber.setText(String.valueOf(index+1));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.step_detail_fragment_view,stepFragment).commit();
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
    public void nextStep(View view){
        if(index+1<steps.getDescriptions().size()){
            StepFragment stepFragment = new StepFragment();
        stepFragment.setStepsAndIndex(steps,++index);
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
            stepFragment.setStepsAndIndex(steps, --index);
            indexNumber.setText(String.valueOf(index+1));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.step_detail_fragment_view, stepFragment).commit();
        }else{
            Toast.makeText(this, "task not possible", Toast.LENGTH_SHORT).show();
        }
    }
}
