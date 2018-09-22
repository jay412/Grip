package com.herokuapp.jordan_chau.grip.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;

import java.util.ArrayList;

public class CalculateBillWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private ArrayList<ReceiptItem> mItems;
    private int mNumberItems;
    private int appWidgetId;

    public CalculateBillWidgetRemoteViewsFactory(Context applicationContext, Intent intent){
        mContext = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    @Override
    public void onCreate() {
        mItems = new ArrayList<>();
        mItems.add(new ReceiptItem(3, "test", 3.00));
        mItems.add(new ReceiptItem(3, "test", 3.00));
        mItems.add(new ReceiptItem(3, "test", 3.00));
    }

    @Override
    public void onDataSetChanged() {
       //final long identityToken = Binder.clearCallingIdentity();
       //mNumberItems = mItems.size();
       //Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mNumberItems;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || mItems == null || mItems.get(position) == null) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_widget_bill_item);
        rv.setTextViewText(R.id.tv_widget_ri_quantity, Integer.toString(mItems.get(position).getQuantity()));
        rv.setTextViewText(R.id.tv_widget_ri_price, Double.toString(mItems.get(position).getPrice()));

        return rv;
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
