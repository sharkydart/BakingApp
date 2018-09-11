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
import com.google.android.exoplayer2.ui.PlayerView;

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
    VideoHelper theVideoHelper;

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

        if(mTheStep != null)
        {
            TextView txtDesc = rootView.findViewById(R.id.tvStepDescription);
            txtDesc.setText(mTheStep.getTheDescription());
            txtDesc.setVisibility(View.VISIBLE);

            if(mTheStep.getTheVideoURL() != null && !mTheStep.getTheVideoURL().isEmpty()) {
                theVideoHelper = new VideoHelper(getContext(), (PlayerView) rootView.findViewById(R.id.pvVideo), mTheStep.getTheVideoURL());
                theVideoHelper.getVideoInto(mTheStep.getTheVideoURL());
            }else{
                rootView.findViewById(R.id.pvVideo).setVisibility(View.GONE);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
