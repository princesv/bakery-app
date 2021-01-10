package com.prince.bakeryapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StepFragment extends Fragment {

    Steps mSteps;
    int mIndex;
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mExoPlayer;
    TextView noVideoText;
    static final String STEPS_JSON = "steps-json";
    static final String INDEX = "index";
    static final String PLAYER_POSITION = "player-position";
    static final String PLAYER_STATE = "player-state";
    String stepsJson;
    long position = 0;
    boolean state = true;

    public void setStepsAndIndex(String steps, int index) {
        stepsJson = steps;
        mIndex = index;
    }



    // Mandatory empty constructor
    public StepFragment() {
    }



    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(savedInstanceState != null){
            stepsJson = savedInstanceState.getString(STEPS_JSON);
            mIndex = savedInstanceState.getInt(INDEX);
            position = savedInstanceState.getLong(PLAYER_POSITION);
            state = savedInstanceState.getBoolean(PLAYER_STATE);
        }

        final View rootView = inflater.inflate(R.layout.step_detail, container, false);
        TextView descriptionTextView = rootView.findViewById(R.id.step_description);
        noVideoText = rootView.findViewById(R.id.no_video_text);
        mPlayerView = rootView.findViewById(R.id.video_player);
        mSteps = getStepsFromJson(stepsJson);
        descriptionTextView.setText(mSteps.getDescriptions().get(mIndex));
        initializePlayer(Uri.parse(mSteps.getUrls().get(mIndex)));
        // Return the root view
        return rootView;
    }
    void initializePlayer(Uri mediaUri){
        if (mExoPlayer == null && !mediaUri.toString().equals("")) {
            // Create an instance of the ExoPlayer.
            showVideoPlayer();
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(position);
            mExoPlayer.setPlayWhenReady(state);

        }else{
            showNoVideoMessage();
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
    void showNoVideoMessage(){
        noVideoText.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.INVISIBLE);
    }
    void showVideoPlayer(){
        mPlayerView.setVisibility(View.VISIBLE);
        noVideoText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
            state = mExoPlayer.getPlayWhenReady();
        }
        if(Util.SDK_INT <= 23){
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
            state = mExoPlayer.getPlayWhenReady();
        }
        if(Util.SDK_INT > 23){
            releasePlayer();
        }
    }



    private void releasePlayer() {
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("inside onSavedState",".......................................//////////........");
        outState.putString(STEPS_JSON,stepsJson);
        outState.putInt(INDEX,mIndex);
        outState.putLong(PLAYER_POSITION,position);
        outState.putBoolean(PLAYER_STATE,state);

    }
}
