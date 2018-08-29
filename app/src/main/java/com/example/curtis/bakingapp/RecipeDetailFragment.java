package com.example.curtis.bakingapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.curtis.bakingapp.model.Recipe;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String THE_RECIPE_ID = "item_id";

    private Recipe mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            if (getArguments().containsKey(THE_RECIPE_ID)) {
                mItem = getArguments().getParcelable(THE_RECIPE_ID);

                Activity activity = this.getActivity();
                if(activity != null) {
                    CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(mItem.getTheName());
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.recipe_detail)).setText(mItem.getIngredientsDump());
            //Log.d("fart", mItem.getIngredientsDump());
            //((TextView) rootView.findViewById(R.id.recipe_detail)).append(mItem.getStepsDump());
            //Log.d("fart", mItem.getStepsDump());
        }

        return rootView;
    }
}
