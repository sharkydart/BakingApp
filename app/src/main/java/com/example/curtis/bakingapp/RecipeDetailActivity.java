package com.example.curtis.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.curtis.bakingapp.model.Recipe;
import com.example.curtis.bakingapp.model.Step;
import com.example.curtis.bakingapp.StepFragment.OnListFragmentInteractionListener;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity implements StepFragment.OnListFragmentInteractionListener{

    private Recipe mTheRecipe;
    private RecyclerView mTheRecyclerView;
    private StepsAdapter mStepsAdapter;
    private OnListFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //setup for fancy look/feel
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            mTheRecipe = getIntent().getParcelableExtra(RecipeDetailFragment.THE_RECIPE_ID);
            arguments.putParcelable(RecipeDetailFragment.THE_RECIPE_ID, mTheRecipe);

            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
        else{
            mTheRecipe = savedInstanceState.getParcelable(RecipeDetailFragment.THE_RECIPE_ID);
            CollapsingToolbarLayout temp = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
            temp.setTitle(mTheRecipe.getTheName());
        }

        mTheRecyclerView = findViewById(R.id.rvStep_list);
        assert mTheRecyclerView != null;
        setupRecyclerView(mTheRecyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mStepsAdapter = new StepsAdapter(mTheRecipe.getTheSteps(), mListener);
        recyclerView.setAdapter(mStepsAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeDetailFragment.THE_RECIPE_ID, mTheRecipe);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(View v, Step clickedStep) {
        Log.d("fart", "clicked something");
        if(clickedStep != null) {
            Log.d("fart", "fragment interacted with: step " + clickedStep.getTheID() + "\n" + clickedStep.getTheShortDescription());
            //TODO: control video playback in some fashion?
            //TODO: possibly need to send the view obj as well - something to get the xpVideo, etc.
            Log.d("fart", "Video URL: " + clickedStep.getTheVideoURL());

            if (v != null) {
                TextView temp = (TextView) v.findViewById(R.id.tvShortDescription);
                temp.setText("BARF");
            }
        }
    }
}
