package com.example.curtis.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetRVService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //return remote view factory here
        return new WidgetRVFactory(this, intent);
    }

}
