package com.example.curtis.bakingapp.recyclerviewstuff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.curtis.bakingapp.R;
import com.example.curtis.bakingapp.RecipeDetailActivity;
import com.example.curtis.bakingapp.RecipeDetailFragment;
import com.example.curtis.bakingapp.RecipeListActivity;
import com.example.curtis.bakingapp.model.Recipe;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private final RecipeListActivity mParentActivity;
    private final ArrayList<Recipe> mRecipesRV;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe theRecipe = (Recipe) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(RecipeDetailFragment.THE_RECIPE_ID, theRecipe);
                arguments.putBoolean(RecipeListActivity.TWO_PANE, true);
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailFragment.THE_RECIPE_ID, theRecipe);
                intent.putExtra(RecipeListActivity.TWO_PANE, mTwoPane);
                context.startActivity(intent);
            }
        }
    };

    public RecipesAdapter(RecipeListActivity parent, boolean twoPane, ArrayList<Recipe> items) {
        mRecipesRV = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(mRecipesRV.get(position).getTheName());
        String contentView = "Ingredients: " + mRecipesRV.get(position).getTheIngredients().size() + " Serves: " + mRecipesRV.get(position).getTheServings();
        holder.mContentView.setText(contentView);

        holder.itemView.setTag(mRecipesRV.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mRecipesRV.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
