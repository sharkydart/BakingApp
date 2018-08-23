package com.example.curtis.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.curtis.bakingapp.model.Recipe;
import com.example.curtis.bakingapp.recyclerviewstuff.RecipesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * dTODO 1: connect to url - "go.udacity.com/android-baking-app-json" to get main set of data, using code from Popular Movies v2

 * dTODO 2: make fragments display basic Recipe info

 * TODO 3: use ExoPlayer/MediaPlayer from Classical Music Quiz to load/control video

 * TODO 4: create a widget

 * TODO 5: use a 3rd party to do something
 *
 * TODO 6:
 */

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private String mSPrefJSON;
    private ArrayList<Recipe> mRecipes;
    private RecyclerView mTheRecyclerView;
    private RecipesAdapter mRecipesAdapter;

    public static final String MYPREFS = "MyPreferences";
    public static final String BAKING_JSON = "baking json feed";
    public static final String RECIPES_AL = "arraylist of parsed recipes";
    SharedPreferences mSharedPreferences;

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        //save the items that are currently in the adapter
        state.putParcelableArrayList(RECIPES_AL, mRecipes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


//        if(savedInstanceState != null && savedInstanceState.containsKey(RECIPES_AL)) {
//            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES_AL);
//            mRecipesAdapter.notifyDataSetChanged();
//        }
//        else
            loadRecipeData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mTheRecyclerView = findViewById(R.id.recipe_list);
        assert mTheRecyclerView != null;
        setupRecyclerView(mTheRecyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecipesAdapter = new RecipesAdapter(this, mRecipes, mTwoPane);
        recyclerView.setAdapter(mRecipesAdapter);
    }

    private void loadRecipeData() {
//        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        //TODO: provide a way to force refresh/"pull from network"
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSPrefJSON = mSharedPreferences.getString(BAKING_JSON, null);
        if(mSPrefJSON != null)
            Log.d("fart", mSPrefJSON);

        mRecipes = new ArrayList<>();
        new FetchRecipesTask().execute(mSPrefJSON);
    }
    public URL buildBakingUrl() {
        String baseurl = this.getString(R.string.recipes_url);
        Uri builtUri = Uri.parse(baseurl).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public class FetchRecipesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if (params[0] != null)
                return params[0];
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager != null) {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        URL recipesURL = buildBakingUrl();
                        try {
                            return getResponseFromUrl(recipesURL);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }
            }catch(NullPointerException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String theData) {
            if (theData != null) {
                if(mSPrefJSON == null){
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(BAKING_JSON, theData); //trying to save theData to the shared preferences
                    editor.apply();
                }
                try {
                    //parse theData json
                    mRecipes.clear();
                    JSONArray results = new JSONArray(theData);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject currentObj = results.getJSONObject(i);
                        Recipe tempRecipe = new Recipe(currentObj);
                        mRecipes.add(tempRecipe);
                    }

                    //update adapter
                    mRecipesAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RecipeListActivity.this, "Data Issue", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(RecipeListActivity.this, "Network Issue", Toast.LENGTH_LONG).show();
            }
        }
    }

}
