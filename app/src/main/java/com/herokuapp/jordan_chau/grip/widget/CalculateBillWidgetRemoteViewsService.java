package com.herokuapp.jordan_chau.grip.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class CalculateBillWidgetRemoteViewsService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CalculateBillWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
