package com.example.curtis.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.example.curtis.bakingapp.R;
import com.example.curtis.bakingapp.RecipeDetailFragment;
import com.example.curtis.bakingapp.RecipeListActivity;
import com.example.curtis.bakingapp.model.Ingredient;
import com.example.curtis.bakingapp.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WidgetRVFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Recipe> collection = new ArrayList<>();
    private Context theContext;
    private Intent theIntent;

    public WidgetRVFactory(Context context, Intent intent) {
        this.theContext = context;
        this.theIntent = intent;
    }

    @Override
    public int getCount() {
        int theReturnCount;
        if(BakingWidgetProvider.mTheIngredients != null && BakingWidgetProvider.mTheIngredients.size() > 0)
            theReturnCount = BakingWidgetProvider.mTheIngredients.size();
        else
            theReturnCount = collection.size();
        return theReturnCount;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(theContext.getPackageName(), android.R.layout.simple_list_item_1);

//        remoteView.setTextViewText(android.R.id.text1, collection.get(position).getTheName());
        if(BakingWidgetProvider.mTheIngredients != null && BakingWidgetProvider.mTheIngredients.size() > 0) {
            Log.d("fart", "ingredient " + position + "created");
            remoteView.setTextViewText(android.R.id.text1, BakingWidgetProvider.mTheIngredients.get(position).getInfo());
            remoteView.setTextColor(android.R.id.text1, Color.BLUE);
            final Intent thisIntent = new Intent();
            thisIntent.setAction(BakingWidgetProvider.ACTION_GOBACK);
            remoteView.setOnClickFillInIntent(android.R.id.text1, thisIntent);
        }
        else {
            remoteView.setTextViewText(android.R.id.text1, collection.get(position).getTheName());
            remoteView.setTextColor(android.R.id.text1, Color.BLACK);
            Log.d("fart", "recipe " + position + "created");

            //adds information unique to the view item at the position into a bundle, which is put in the intent for the list item
            // ...defining the unique action
            final Intent fillInIntent = new Intent();
            fillInIntent.setAction(BakingWidgetProvider.ACTION_TOAST);
            final Bundle theBundle = new Bundle();
            theBundle.putParcelableArrayList(BakingWidgetProvider.THE_INGREDIENTS, collection.get(position).getTheIngredients());
            fillInIntent.putExtras(theBundle);
            remoteView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);
        }
        return remoteView;
    }

    @Override
    public void onCreate() {
        if(BakingWidgetProvider.mTheIngredients == null || BakingWidgetProvider.mTheIngredients.size() == 0)
            initData();
    }

    @Override
    public void onDataSetChanged() {
        if(BakingWidgetProvider.mTheIngredients == null || BakingWidgetProvider.mTheIngredients.size() == 0)
            initData();
    }

    private void initData(){
        collection.clear();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(theContext);
        String theRecipes = sharedPref.getString(RecipeListActivity.BAKING_JSON, "A recipe doesn't have a name.");

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


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
