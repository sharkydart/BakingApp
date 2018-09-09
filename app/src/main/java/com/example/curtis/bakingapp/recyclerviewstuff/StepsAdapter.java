package com.example.curtis.bakingapp.recyclerviewstuff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.curtis.bakingapp.R;
import com.example.curtis.bakingapp.RecipeDetailFragment;
import com.example.curtis.bakingapp.RecipeListActivity;
import com.example.curtis.bakingapp.StepDetailActivity;
import com.example.curtis.bakingapp.StepDetailFragment;
import com.example.curtis.bakingapp.StepsFragment;
import com.example.curtis.bakingapp.model.Recipe;
import com.example.curtis.bakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private final Recipe mTheRecipe;
    private final AppCompatActivity mParentActivity;
    private final ArrayList<Step> mValues;
//    private final OnListFragmentInteractionListener mListener;
    private final boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step theStep = (Step)view.getTag();
            if (mTwoPane) {
                //Prepare the details of the step for the right side fragment
                Bundle rightArgs = new Bundle();
                rightArgs.putParcelable(StepsFragment.THE_STEP_ID, theStep);
                rightArgs.putBoolean(RecipeListActivity.TWO_PANE, mTwoPane);
                StepDetailFragment rightFragment = new StepDetailFragment();
                rightFragment.setArguments(rightArgs);

                //Prepare the steps list for the left side fragment
                Bundle leftArgs = new Bundle();
                leftArgs.putParcelable(RecipeDetailFragment.THE_RECIPE_ID, mTheRecipe);
                leftArgs.putBoolean(RecipeListActivity.TWO_PANE, mTwoPane);
                RecipeDetailFragment leftFragment = new RecipeDetailFragment();
                leftFragment.setArguments(leftArgs);

                if(mParentActivity.findViewById(R.id.recipe_list).getVisibility() != View.GONE) {
                    mParentActivity.findViewById(R.id.recipe_list).setVisibility(View.GONE);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_menu_container, leftFragment)
                            .replace(R.id.recipe_detail_container, rightFragment)
                            .commit();
                }
                else{
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, rightFragment)
                            .commit();
                }
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, StepDetailActivity.class);
                intent.putExtra(StepDetailActivity.THE_STEPS_ARRAY, mValues);
                intent.putExtra(StepsFragment.THE_STEP_ID, theStep);
                intent.putExtra(RecipeListActivity.TWO_PANE, false);
                context.startActivity(intent);
            }
        }
    };

    public StepsAdapter(AppCompatActivity parent, boolean twoPane, Recipe theRecipe/*ArrayList<Step> items*//*, OnListFragmentInteractionListener listener*/) {
        mTheRecipe = theRecipe;
        mValues = theRecipe.getTheSteps();
        mParentActivity = parent;
//        mListener = listener;
        mTwoPane = twoPane;
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

//        Log.d("fart", "imgpath:" + imgpath);

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
