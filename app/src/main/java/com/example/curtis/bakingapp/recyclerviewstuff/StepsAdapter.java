package com.example.curtis.bakingapp.recyclerviewstuff;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.curtis.bakingapp.R;
import com.example.curtis.bakingapp.StepFragment.OnListFragmentInteractionListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private final ArrayList<Step> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step theStep = (Step)view.getTag();
            if (mTwoPane) {
                getVideoInto(theStep.getTheVideoURL());
//                Bundle arguments = new Bundle();
//                arguments.putParcelable(StepDetailFragment.LOAD_STEP_ID, theStep);
//                StepDetailFragment fragment = new StepDetailFragment();
//                fragment.setArguments(arguments);
//                mParentActivity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.step_detail_container, fragment)
//                        .commit();
            } else {
                //load video
                getVideoInto(theStep.getTheVideoURL());
//                Context context = view.getContext();
//                Intent intent = new Intent(context, StepDetailActivity.class);
//                intent.putExtra(StepDetailFragment.LOAD_STEP_ID, theStep);
//                context.startActivity(intent);
            }
        }
    };

    public StepsAdapter(ArrayList<Step> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mTwoPane = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTheStepItem = mValues.get(position);

        //set the pieces of the step?
        holder.mShortDescView.setText(mValues.get(position).getTheShortDescription());
        holder.mLongDescView.setText(mValues.get(position).getTheDescription());
        //load thumbnail
        ImageView tmpIMG = holder.mThumbnailView;
        getImageInto(tmpIMG, mValues.get(position).getTheThumbnailURL());

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }
    private void getImageInto(ImageView theImgView, String imgpath){
        theImgView.setScaleType(ImageView.ScaleType.FIT_START);
        theImgView.setAdjustViewBounds(true);

        Log.d("fart", "imgpath:" + imgpath);

        if(!imgpath.isEmpty()) {
            Picasso.get()
                    .load(imgpath)
                    .into(theImgView);
            theImgView.setVisibility(View.VISIBLE);
        }
        else{
            theImgView.setVisibility(View.GONE);
        }
    }
    private void getVideoInto(String theVideoUrl){
        if(!theVideoUrl.isEmpty()) {
            //call static method, passing theVideoUrl
//            if(mSimplePlayer == null) {
//                // 1. Create a default TrackSelector
//                //Handler mainHandler = new Handler();
//                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//                TrackSelection.Factory videoTrackSelectionFactory =
//                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
//                DefaultTrackSelector trackSelector =
//                        new DefaultTrackSelector(videoTrackSelectionFactory);
//
//                // 2. Create the player
//                mSimplePlayer = ExoPlayerFactory.newSimpleInstance(mThePlayerView.getContext(), trackSelector);
//                mThePlayerView.setPlayer(mSimplePlayer);
//
//                // Produces DataSource instances through which media data is loaded.
//                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
//                        mThePlayerView.getContext(),
//                        Util.getUserAgent(mThePlayerView.getContext(), "com.example.curtis.bakingapp"));
//                // This is the MediaSource representing the media to be played.
//                mTheMediaFactory = new ExtractorMediaSource.Factory(dataSourceFactory);
//
//                // This is the MediaSource representing the media to be played.
//                MediaSource videoSource = mTheMediaFactory.createMediaSource(Uri.parse(theVideoUrl));
//                // Prepare the player with the source.
//                mSimplePlayer.prepare(videoSource);
//            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mShortDescView;
        public final TextView mLongDescView;
        public Step mTheStepItem;
        public ImageView mThumbnailView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnailView = view.findViewById(R.id.ivThumbnail);
            mShortDescView = view.findViewById(R.id.tvShortDescription);
            mLongDescView = view.findViewById(R.id.tvDescription);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTheStepItem.getTheShortDescription() + "'";
        }
    }
}
