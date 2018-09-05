package com.example.curtis.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {

    public static final String THE_STEPS_ARRAY = "com.example.curtis.bakingapp.THESTEPSARRAY";
    private ArrayList<Step> mStepsArray;
    private Step mTheStep;
    private boolean mTwoPane;
    private final static String VID_PLAY = "vidplaywhenready";
    private final static String VID_POS = "vidplayposition";
    private final static String VID_WIND = "vidplaycurrentwindow";
    boolean mVidPlayWhenReady;
    long mVidPlayPosition;
    int mVidPlayWindow;
//    SimpleExoPlayer mSimplePlayer;
//    PlayerView mThePlayerView;
//    ExtractorMediaSource.Factory mTheMediaFactory;
    VideoHelper theVideoHelper;
//    OnSwitchStepsListener mCallback;
//    public interface OnSwitchStepsListener{
//        void OnSwitchStepClick(Step theStep, boolean nextIfTrueBackIfFalse);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_step_detail);

//        mCallback = (OnSwitchStepsListener)getParent();

        if (savedInstanceState == null){
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            mStepsArray = getIntent().getParcelableArrayListExtra(THE_STEPS_ARRAY);
            arguments.putParcelableArrayList(THE_STEPS_ARRAY, mStepsArray);
            mTheStep = getIntent().getParcelableExtra(StepsFragment.THE_STEP_ID);
            arguments.putParcelable(StepsFragment.THE_STEP_ID, mTheStep);
            mTwoPane = getIntent().getBooleanExtra(RecipeListActivity.TWO_PANE, false);
            arguments.putBoolean(RecipeListActivity.TWO_PANE, mTwoPane);

            if(mTheStep != null && mTheStep.getTheVideoURL() != null && !mTheStep.getTheVideoURL().isEmpty()) {
                theVideoHelper = new VideoHelper((PlayerView) findViewById(R.id.pvVideo), mTheStep.getTheVideoURL());
            }else{
                findViewById(R.id.pvVideo).setVisibility(View.GONE);
            }

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
            mStepsArray = savedInstanceState.getParcelableArrayList(THE_STEPS_ARRAY);
            //get videohelper?
            if(theVideoHelper == null)
                theVideoHelper = new VideoHelper((PlayerView) findViewById(R.id.pvVideo), mTheStep.getTheVideoURL());
            theVideoHelper.setPlayAndPosAndWindow(
                    savedInstanceState.getBoolean(VID_PLAY),
                    savedInstanceState.getLong(VID_POS),
                    savedInstanceState.getInt(VID_WIND)
            );
        }

        if(mTheStep != null) {
            TextView txtDesc = findViewById(R.id.tvStepDescription);
            txtDesc.setText(mTheStep.getTheDescription());
//            txtDesc.setVisibility(View.VISIBLE);
            findViewById(R.id.btnPrevious).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mCallback.OnSwitchStepClick(mTheStep, false);
                    switchStepClick(false);
                }
            });
            findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mCallback.OnSwitchStepClick(mTheStep, true);
                    switchStepClick(true);
                }
            });

            if(mTheStep.getTheVideoURL() != null && !mTheStep.getTheVideoURL().isEmpty()) {
                theVideoHelper.getVideoInto(mTheStep.getTheVideoURL());
            }
            else
                findViewById(R.id.pvVideo).setVisibility(View.GONE);
        }
    }

    private void switchStepClick(boolean nextIfTrueBackIfFalse) {
        if(mStepsArray != null && mTheStep != null){
            int curIndex = mTheStep.getTheID();
            Log.d("fart", "clicked index: " + curIndex);
            Step loadStep;
            if(nextIfTrueBackIfFalse){
                //next
                if(curIndex < mStepsArray.size()-1) {
                    curIndex = curIndex + 1;
                    loadStep = mStepsArray.get(curIndex);
                }else{
                    //else: currently at the end, so can't go forward
                    return;
                }
            }else{
                //prev
                if(curIndex > 0) {
                    curIndex = curIndex - 1;
                    loadStep = mStepsArray.get(curIndex);
                }else {
                    //else: currently at the beginning, so can't go back
                    return;
                }
            }

            Context context = this;
            Intent intent = new Intent(context, StepDetailActivity.class);
            intent.putExtra(StepDetailActivity.THE_STEPS_ARRAY, mStepsArray);
            intent.putExtra(StepsFragment.THE_STEP_ID, loadStep);
            intent.putExtra(RecipeListActivity.TWO_PANE, false);
            context.startActivity(intent);
        }
        else
        {
            Log.d("fart", "not working");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.crdDescription).setVisibility(View.GONE);
            findViewById(R.id.llButtons).setVisibility(View.GONE);
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.crdDescription).setVisibility(View.VISIBLE);
            findViewById(R.id.llButtons).setVisibility(View.VISIBLE);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("fart", "onSaveInstanceState");
        //save stuff here
//        outState.putParcelableArrayList();
//        outState.putParcelable();
        if(theVideoHelper != null && theVideoHelper.getmSimplePlayer() != null){
            mVidPlayPosition = theVideoHelper.getmSimplePlayer().getCurrentPosition();
            mVidPlayWindow = theVideoHelper.getmSimplePlayer().getCurrentWindowIndex();
            mVidPlayWhenReady = theVideoHelper.getmSimplePlayer().getPlayWhenReady();
            outState.putBoolean(VID_PLAY, mVidPlayWhenReady);
            outState.putInt(VID_WIND, mVidPlayWindow);
            outState.putLong(VID_POS, mVidPlayPosition);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("fart", "OnPause");
        if(theVideoHelper != null && theVideoHelper.getmSimplePlayer() != null) {
            mVidPlayPosition = theVideoHelper.getmSimplePlayer().getCurrentPosition();
            theVideoHelper.stopAndDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("fart", "OnResume");
        if(theVideoHelper.getmSimplePlayer() != null && !mTheStep.getTheVideoURL().isEmpty()) {
            if(theVideoHelper == null)
                theVideoHelper = new VideoHelper((PlayerView) findViewById(R.id.pvVideo), mTheStep.getTheVideoURL());
            //initplayer
            theVideoHelper.getVideoInto(mTheStep.getTheVideoURL());
            //seekto play position
            theVideoHelper.setPlayAndPosAndWindow(mVidPlayWhenReady, mVidPlayPosition, mVidPlayWindow);
            //setplaywhenready
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("fart", "OnDestroy");
        if(theVideoHelper != null)
            theVideoHelper.stopAndDestroy();
    }
}
