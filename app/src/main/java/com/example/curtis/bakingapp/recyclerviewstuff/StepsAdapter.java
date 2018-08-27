package com.example.curtis.bakingapp.recyclerviewstuff;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.curtis.bakingapp.R;
import com.example.curtis.bakingapp.StepFragment.OnListFragmentInteractionListener;
import com.example.curtis.bakingapp.model.Step;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private final ArrayList<Step> mValues;
    private final OnListFragmentInteractionListener mListener;

    public StepsAdapter(ArrayList<Step> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        getImageInto(holder.mThumbnailView, mValues.get(position).getTheThumbnailURL());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(v, holder.mTheStepItem);
                }
            }
        });
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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mShortDescView;
        public final TextView mLongDescView;
        public Step mTheStepItem;
        public SimpleExoPlayerView mPlayerView;
        public ImageView mThumbnailView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPlayerView = view.findViewById(R.id.xpVideo);
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
