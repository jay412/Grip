package com.herokuapp.jordan_chau.grip.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.herokuapp.jordan_chau.grip.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CalculateBillWidgetConfigureActivity CalculateBillWidgetConfigureActivity}
 */
public class CalculateBillWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //TODO round values
        String subtotal = "$" + CalculateBillWidgetConfigureActivity.loadSubTotalPref(context, appWidgetId);
        String tax = "$" + CalculateBillWidgetConfigureActivity.loadTaxPref(context, appWidgetId);
        String tip = "$" + CalculateBillWidgetConfigureActivity.loadTipPref(context, appWidgetId);
        String grandtotal = "$" + CalculateBillWidgetConfigureActivity.loadGrandTotalPref(context, appWidgetId);
        String personPay = "$" + CalculateBillWidgetConfigureActivity.loadPersonPayPref(context, appWidgetId);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calculate_bill_widget);

        views.setTextViewText(R.id.tv_widget_item_subtotal, subtotal);
        views.setTextViewText(R.id.tv_widget_item_tax, tax);
        views.setTextViewText(R.id.tv_widget_item_tip, tip);
        views.setTextViewText(R.id.tv_widget_item_grand_total, grandtotal);
        views.setTextViewText(R.id.tv_widget_item_person_pay, personPay);

        //Intent intent = new Intent(context, CalculateBillWidgetRemoteViewsService.class);
        //views.setRemoteAdapter(R.id.widget_item_list, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            CalculateBillWidgetConfigureActivity.deleteTaxTipPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

