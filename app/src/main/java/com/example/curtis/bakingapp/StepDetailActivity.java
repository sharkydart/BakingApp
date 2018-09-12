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
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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
    SimpleExoPlayer mSimplePlayer;
    PlayerView mThePlayerView;
//    ExtractorMediaSource.Factory mTheMediaFactory;
//    OnSwitchStepsListener mCallback;
//    public interface OnSwitchStepsListener{
//        void OnSwitchStepClick(Step theStep, boolean nextIfTrueBackIfFalse);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_step_detail);

//        mCallback = (OnSwitchStepsListener)getParent();
        mThePlayerView = findViewById(R.id.pvVideo);

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
                if(mSimplePlayer == null)
                    initPlayer();
//                mThePlayerView.setVisibility(View.VISIBLE);
            }else{
                mThePlayerView.setVisibility(View.GONE);
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
            Log.d("fart", "-load vid state from saved instance-");
            mVidPlayPosition = savedInstanceState.getLong(VID_POS);
            mVidPlayWhenReady = savedInstanceState.getBoolean(VID_PLAY);
            mVidPlayWindow = savedInstanceState.getInt(VID_WIND);

            if(mSimplePlayer == null) {
                Log.d("fart", "onCreate ->> savedInstanceState != null AND mSimplePlayer != null");
                initPlayer();
            }
        }

        loadButtons();
    }

    void loadButtons() {
        TextView txtDesc = findViewById(R.id.tvStepDescription);
        txtDesc.setText(mTheStep.getTheDescription());

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
    }

    void loadNewVideo(){
        if(mTheStep.getTheVideoURL() != null && !mTheStep.getTheVideoURL().isEmpty()) {
            Log.d("fart", "url = " + mTheStep.getTheVideoURL());
            prepareSimplePlayerWithSource(mTheStep.getTheVideoURL());
            mSimplePlayer.setPlayWhenReady(false);
            mThePlayerView.setVisibility(View.VISIBLE);
        }
        else
            mThePlayerView.setVisibility(View.GONE);
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
            //pause the player
            mSimplePlayer.setPlayWhenReady(false);

            //changing data to new step
            mTheStep = loadStep;
            //changing buttons
            loadButtons();
            //changing video
            loadNewVideo();
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
        outState.putParcelableArrayList(THE_STEPS_ARRAY, mStepsArray);
        outState.putParcelable(StepsFragment.THE_STEP_ID, mTheStep);

        outState.putBoolean(VID_PLAY, mVidPlayWhenReady);
        outState.putInt(VID_WIND, mVidPlayWindow);
        outState.putLong(VID_POS, mVidPlayPosition);
        //try mSimplePlayer properties if those don't work
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("fart", "instance state restored");
        mVidPlayPosition = savedInstanceState.getLong(VID_POS);
        mVidPlayWhenReady = savedInstanceState.getBoolean(VID_PLAY);
        mVidPlayWindow = savedInstanceState.getInt(VID_WIND);
    }

//    private void stopAndReleaseVideoHelper(){
//        if(theVideoHelper != null && mSimplePlayer != null) {
//            Log.d("fart", "actually should stop");
//            mVidPlayPosition = mSimplePlayer.getCurrentPosition();
//            mVidPlayWindow = mSimplePlayer.getCurrentWindowIndex();
//            mVidPlayWhenReady = mSimplePlayer.getPlayWhenReady();
//            theVideoHelper.stopAndDestroy(mSimplePlayer);
//            //mSimplePlayer = null;
//            theVideoHelper = null;
//        }
//        else{
//            Log.d("fart", "refusing to STOP");
//        }
//    }

    //TODO: each of these implemented to reflect what I saw in tutorials
    @Override
    protected void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23){
            Log.d("fart", "onPause - STOPPING");
//            stopAndReleaseVideoHelper();
            releasePlayer();
        }
        else{
            Log.d("fart", "onPause - did not stop/release: SDK > 23");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23){
            Log.d("fart", "onStop - STOPPING");
//            stopAndReleaseVideoHelper();
            releasePlayer();
        }
        else{
            Log.d("fart", "onStop - did not stop/release: SDK <= 23");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23){
            Log.d("fart", "onStart[ initPlayer ]");
//            if(mTheStep != null && mTheStep.getTheVideoURL() != null && !mTheStep.getTheVideoURL().isEmpty()) {
//                theVideoHelper = new VideoHelper(this, mThePlayerView, mTheStep.getTheVideoURL());
//                theVideoHelper.setmThePlayerView(mThePlayerView);
//            }
            initPlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.SDK_INT <= 23 || mSimplePlayer == null){
            Log.d("fart", "onResume[ initPlayer ]");
//            if(mTheStep != null && mTheStep.getTheVideoURL() != null && !mTheStep.getTheVideoURL().isEmpty()) {
//                theVideoHelper = new VideoHelper(this,  mThePlayerView, mTheStep.getTheVideoURL());
//                theVideoHelper.setmThePlayerView(mThePlayerView);
//            }
            initPlayer();
        }
    }

    private void initPlayer(){
        mSimplePlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());
        mThePlayerView.setPlayer(mSimplePlayer);
        prepareSimplePlayerWithSource(mTheStep.getTheVideoURL());
        mSimplePlayer.seekTo(mVidPlayWindow, mVidPlayPosition);
        mSimplePlayer.setPlayWhenReady(mVidPlayWhenReady);
/*
        Uri videoLink = Uri.parse(mTheStep.getTheVideoURL());
        MediaSource theMediaSource = mediaSourceFromLink(videoLink);
        mSimplePlayer.prepare(theMediaSource, true, false);
*/
    }
    private void prepareSimplePlayerWithSource(String theUrlSource){
        Uri videoLink = Uri.parse(theUrlSource);
        MediaSource theMediaSource = mediaSourceFromLink(videoLink);
        mSimplePlayer.prepare(theMediaSource, true, false);
    }
    private MediaSource mediaSourceFromLink(Uri theLink){
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("com.example.curtis.bakingapp")).
                createMediaSource(theLink);
    }

    private void releasePlayer() {
        if (mSimplePlayer != null) {
            mVidPlayPosition = mSimplePlayer.getCurrentPosition();
            mVidPlayWindow = mSimplePlayer.getCurrentWindowIndex();
            mVidPlayWhenReady = mSimplePlayer.getPlayWhenReady();
            mSimplePlayer.release();
            mSimplePlayer = null;
        }
    }
}
