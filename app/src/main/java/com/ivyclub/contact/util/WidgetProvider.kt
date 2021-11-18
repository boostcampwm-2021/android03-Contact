package com.ivyclub.contact.util

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.ivyclub.contact.R
import java.sql.Date


class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds?.forEach {
            val serviceIntent = Intent(context, MyRemoteViewsService::class.java)
            val widget = RemoteViews(context.packageName, R.layout.widget)
            widget.setRemoteAdapter(R.id.lv_widget_plan_list, serviceIntent)

            val currentMonth = Date(System.currentTimeMillis()).getExactMonth()
            widget.setTextViewText(R.id.tv_widget, String.format(context.getString(R.string.format_widget_plan), currentMonth))

            appWidgetManager.updateAppWidget(it, widget)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val mgr = AppWidgetManager.getInstance(context)
            val cn = ComponentName(context, WidgetProvider::class.java)
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lv_widget_plan_list)
        }
    }

    companion object {
        fun sendRefreshBroadcast(context: Context) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            intent.component = ComponentName(context, WidgetProvider::class.java)
            context.sendBroadcast(intent)
        }
    }
}