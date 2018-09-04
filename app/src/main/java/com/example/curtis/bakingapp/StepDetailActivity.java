package com.example.curtis.bakingapp;

import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.curtis.bakingapp.Video.VideoHelper;
import com.example.curtis.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class StepDetailActivity extends AppCompatActivity {

    private Step mTheStep;
    private boolean mTwoPane;
//    SimpleExoPlayer mSimplePlayer;
//    PlayerView mThePlayerView;
//    ExtractorMediaSource.Factory mTheMediaFactory;
    VideoHelper theVideoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            mTheStep = getIntent().getParcelableExtra(StepsFragment.THE_STEP_ID);
            arguments.putParcelable(StepsFragment.THE_STEP_ID, mTheStep);
            mTwoPane = getIntent().getBooleanExtra(RecipeListActivity.TWO_PANE, false);
            arguments.putBoolean(RecipeListActivity.TWO_PANE, mTwoPane);

            theVideoHelper = new VideoHelper((PlayerView)findViewById(R.id.pvVideo), mTheStep.getTheVideoURL());

            if(mTwoPane) {
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);

                //loads into right-side pane
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_detail_container, fragment)
                        .commit();
            }
        }
        else{
            mTwoPane = savedInstanceState.getBoolean(RecipeListActivity.TWO_PANE);
            mTheStep = savedInstanceState.getParcelable(StepsFragment.THE_STEP_ID);
            //get videohelper?
        }

        if(mTheStep != null) {
            TextView txtDesc = findViewById(R.id.tvStepDescription);
            txtDesc.setText(mTheStep.getTheDescription());

            theVideoHelper.getVideoInto(mTheStep.getTheVideoURL());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //save stuff here
    }

}
