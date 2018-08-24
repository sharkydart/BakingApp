package com.example.curtis.bakingapp.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.curtis.bakingapp.R;
import com.example.curtis.bakingapp.RecipeListActivity;
import com.example.curtis.bakingapp.model.Ingredient;
import com.example.curtis.bakingapp.model.Recipe;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_TOAST = "com.example.curtis.bakingapp.ACTION_TOAST";
    public static final String ACTION_GOBACK = "com.example.curtis.bakingapp.ACTION_GOBACK";
    public static final String THE_INGREDIENTS = "com.example.curtis.bakingapp.THE_INGREDIENTS";
    public static ArrayList<Ingredient> mTheIngredients;

    public BakingWidgetProvider(){}

    @Override
    public void onReceive(Context theContext, Intent intent) {
        try{
            if(intent != null && intent.getAction() != null){
                if(intent.getAction().equals(ACTION_TOAST)) {
                    if (intent.hasExtra(THE_INGREDIENTS) && intent.getParcelableArrayListExtra(THE_INGREDIENTS) != null) {
                        mTheIngredients = intent.getParcelableArrayListExtra(THE_INGREDIENTS);

                        AppWidgetManager aWM = AppWidgetManager.getInstance(theContext);
                        int[] appWidgetIds = aWM.getAppWidgetIds(new ComponentName(theContext, BakingWidgetProvider.class));
                        RemoteViews remoteView = new RemoteViews(theContext.getPackageName(), R.layout.baking_widget_provider_layout);
                        for (int widgetId : appWidgetIds) {
                            aWM.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list);
                        }
                        aWM.updateAppWidget(appWidgetIds, remoteView);
                    } else {
                        Toast.makeText(theContext, "no extra or arraylist NULL", Toast.LENGTH_LONG).show();
                    }
                }
                else if(intent.getAction().equals(ACTION_GOBACK)){
                    mTheIngredients.clear();

                    AppWidgetManager aWM = AppWidgetManager.getInstance(theContext);
                    int[] appWidgetIds = aWM.getAppWidgetIds(new ComponentName(theContext, BakingWidgetProvider.class));
                    RemoteViews remoteView = new RemoteViews(theContext.getPackageName(), R.layout.baking_widget_provider_layout);
                    for (int widgetId : appWidgetIds) {
                        aWM.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list);
                    }
                    aWM.updateAppWidget(appWidgetIds, remoteView);
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            Log.d("fart", "something failed in onReceive");
        }
        super.onReceive(theContext, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider_layout);

            Intent raIntent = new Intent(context, WidgetRVService.class);
            raIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);    //don't know why
            raIntent.setData(Uri.parse(raIntent.toUri(Intent.URI_INTENT_SCHEME)));  //don't know why
            views.setRemoteAdapter(R.id.widget_list, raIntent);

            //set up the click for the pie image to open the app
            final Intent appIntent = new Intent(context, RecipeListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_img_launcher, pendingIntent);

            //set up the Intent & PendingIntent for the listview's fillInIntents to work
            final Intent onItemClickIntent = new Intent(context, BakingWidgetProvider.class);
            onItemClickIntent.setData(Uri.parse(onItemClickIntent.toUri(Intent.URI_INTENT_SCHEME)));

            final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onItemClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, onClickPendingIntent);
            Log.d("fart", "going through onUpdate");

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
//
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider_layout);
//
//        Intent raIntent = new Intent(context, WidgetRVService.class);
//        raIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);    //don't know why
//        raIntent.setData(Uri.parse(raIntent.toUri(Intent.URI_INTENT_SCHEME)));  //don't know why
//        views.setRemoteAdapter(R.id.widget_list, raIntent);
//
//        //set up the click for the pie image to open the app
//        final Intent appIntent = new Intent(context, RecipeListActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
//        views.setOnClickPendingIntent(R.id.widget_img_launcher, pendingIntent);
//
//        //set up the Intent & PendingIntent for the listview's fillInIntents to work
//        final Intent onItemClickIntent = new Intent(context, BakingWidgetProvider.class);
//        onItemClickIntent.setAction(ACTION_TOAST);
//        onItemClickIntent.setData(Uri.parse(onItemClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
//
//        final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onItemClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setPendingIntentTemplate(R.id.widget_list, onClickPendingIntent);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

