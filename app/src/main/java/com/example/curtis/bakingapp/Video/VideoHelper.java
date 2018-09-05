package com.example.curtis.bakingapp.Video;

import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SeekParameters;
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

public class VideoHelper {
    SimpleExoPlayer mSimplePlayer;
    PlayerView mThePlayerView;
    ExtractorMediaSource.Factory mTheMediaFactory;
    String theVideoUrl;

    public String getTheVideoUrl() {
        return theVideoUrl;
    }

    public void setTheVideoUrl(String theVideoUrl) {
        this.theVideoUrl = theVideoUrl;
    }

    public PlayerView getmThePlayerView() {
        return mThePlayerView;
    }

    public void setmThePlayerView(PlayerView mThePlayerView) {
        this.mThePlayerView = mThePlayerView;
    }

    public VideoHelper(PlayerView thePlayerView, String initVideoUrl){
        mThePlayerView = thePlayerView;
        theVideoUrl = initVideoUrl;
    }

    public void getVideoInto(String newVideoUrl){
        this.theVideoUrl = newVideoUrl;
        if(theVideoUrl != null && !theVideoUrl.isEmpty()) {
            //call static method, passing theVideoUrl
            if(mSimplePlayer == null && mTheMediaFactory == null) {
                // 1. Create a default TrackSelector
                //Handler mainHandler = new Handler();
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                DefaultTrackSelector trackSelector =
                        new DefaultTrackSelector(videoTrackSelectionFactory);

                // 2. Create the player
                mSimplePlayer = ExoPlayerFactory.newSimpleInstance(mThePlayerView.getContext(), trackSelector);
                mThePlayerView.setPlayer(mSimplePlayer);

                // Produces DataSource instances through which media data is loaded.
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                        mThePlayerView.getContext(),
                        Util.getUserAgent(mThePlayerView.getContext(), "com.example.curtis.bakingapp"));
                // This is the MediaSource representing the media to be played.
                mTheMediaFactory = new ExtractorMediaSource.Factory(dataSourceFactory);
            }
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = mTheMediaFactory.createMediaSource(Uri.parse(theVideoUrl));
            // Prepare the player with the source.
            mSimplePlayer.prepare(videoSource);
        }
    }

    public SimpleExoPlayer getmSimplePlayer() {
        return mSimplePlayer;
    }

    public void stopAndDestroy(){
        if(mSimplePlayer != null) {
            mSimplePlayer.stop();
            mSimplePlayer.release();
            mSimplePlayer = null;
        }
    }
    public void stop(){
        if(mSimplePlayer!= null)
            mSimplePlayer.stop();
    }
    public void setPlayAndPosAndWindow(boolean playState, long resumePosition, int curWindow){
        if(mSimplePlayer != null) {
            mSimplePlayer.seekTo(curWindow,resumePosition);
            mSimplePlayer.setPlayWhenReady(playState);
        }
    }
}
