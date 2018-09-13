package com.example.curtis.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.curtis.bakingapp.Video.VideoHelper;
import com.example.curtis.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.example.curtis.bakingapp.StepDetailActivity.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailFragment extends Fragment {

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

//    private OnSwitchStepsListener mCallback;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(Step param1, boolean param2) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(StepsFragment.THE_STEP_ID, param1);
        args.putBoolean(RecipeListActivity.TWO_PANE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            if (getArguments().containsKey(StepsFragment.THE_STEP_ID)) {
                mTheStep = getArguments().getParcelable(StepsFragment.THE_STEP_ID);
                mTwoPane = getArguments().getBoolean(RecipeListActivity.TWO_PANE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mThePlayerView = rootView.findViewById(R.id.pvVideo);
        if(mTheStep != null)
        {
            TextView txtDesc = rootView.findViewById(R.id.tvStepDescription);
            txtDesc.setText(mTheStep.getTheDescription());
            txtDesc.setVisibility(View.VISIBLE);

            if(mTheStep.getTheVideoURL() != null && !mTheStep.getTheVideoURL().isEmpty()) {
                initPlayer();
            }else{
                mThePlayerView.setVisibility(View.GONE);
            }

        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mCallback = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23)
            releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23)
            releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Util.SDK_INT <= 23 || mSimplePlayer == null)
            initPlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT >23)
            initPlayer();
    }


    private void initPlayer(){
        mSimplePlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this.getContext()),
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
