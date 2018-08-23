package com.example.curtis.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.example.curtis.bakingapp.R;
import com.example.curtis.bakingapp.RecipeDetailFragment;
import com.example.curtis.bakingapp.RecipeListActivity;
import com.example.curtis.bakingapp.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    public static final String THE_INGREDIENTS = "the ingredients for the recipe";
    private ArrayList<Recipe> collection = new ArrayList<>();
    private Context theContext;
    private Intent theIntent;

    @Override
    public void onCreate() {
        initData();
    }
    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData(){
        collection.clear();
        if(theIntent.getAction() != null && theIntent.getAction().equals(THE_INGREDIENTS))
            Log.d("fart", "action: " + THE_INGREDIENTS + "!!!");

        Bundle theBundle = theIntent.getBundleExtra(THE_INGREDIENTS);
        if(theBundle != null)
            if(theBundle.containsKey(THE_INGREDIENTS))
                Toast.makeText(theContext, "Ingredients!", Toast.LENGTH_LONG).show();
            else
                Log.d("fart", "no key");


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(theContext);
        String theRecipes = sharedPref.getString(RecipeListActivity.BAKING_JSON, "A recipe doesn't have a name.");
//        Log.d("fart", theRecipes);

        if(theRecipes.length() > 0) {
            try {
                JSONArray theRecipesJSON = new JSONArray(theRecipes);
                for (int i = 0; i < theRecipesJSON.length(); i++) {
                    JSONObject currentObj = theRecipesJSON.getJSONObject(i);
                    Recipe tempRecipe = new Recipe(currentObj);
                    collection.add(tempRecipe);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(theContext, "Recipe Problem", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(theContext, "Please load the app first, Miriam", Toast.LENGTH_LONG).show();
    }
    public WidgetDataProvider(Context context, Intent intent) {
        this.theContext = context;
        this.theIntent = intent;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(theContext.getPackageName(), android.R.layout.simple_list_item_1);

        remoteView.setTextViewText(android.R.id.text1, collection.get(position).getTheName());
        remoteView.setTextColor(android.R.id.text1, Color.BLACK);

        //adds information unique to the view item at the position into a bundle, which is put in the intent for the list item
        // ...defining the unique action
        Bundle extras = new Bundle();
        extras.putParcelableArrayList(THE_INGREDIENTS, collection.get(position).getTheIngredients());
        Intent fillInIntent = new Intent();
        fillInIntent.setAction(THE_INGREDIENTS);
        fillInIntent.putExtra(THE_INGREDIENTS, extras);
        remoteView.setOnClickFillInIntent(android.R.layout.simple_list_item_1, fillInIntent);

        return remoteView;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
