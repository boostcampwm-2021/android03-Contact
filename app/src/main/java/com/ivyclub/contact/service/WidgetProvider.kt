package com.ivyclub.contact.service

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.ivyclub.contact.R
import com.ivyclub.contact.service.ContactRemoteViewsFactory.Companion.WIDGET_PLAN_ID
import com.ivyclub.contact.ui.main.MainActivity
import com.ivyclub.contact.util.getExactMonth
import java.sql.Date


class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach {
            val serviceIntent = Intent(context, ContactRemoteViewsService::class.java)
            val widget = RemoteViews(context.packageName, R.layout.widget)
            widget.setRemoteAdapter(R.id.lv_widget_plan_list, serviceIntent)

            val currentMonth = Date(System.currentTimeMillis()).getExactMonth()
            widget.setTextViewText(
                R.id.tv_widget,
                String.format(context.getString(R.string.format_widget_plan), currentMonth)
            )

            val itemClickIntent = Intent(context, WidgetProvider::class.java).apply {
                action = ACTION_ITEM_CLICK
            }
            val itemClickPendingIntent =
                PendingIntent.getBroadcast(context, 0, itemClickIntent, 0)

            widget.setPendingIntentTemplate(R.id.lv_widget_plan_list, itemClickPendingIntent)

            val refreshClickIntent = Intent(context, WidgetProvider::class.java).apply {
                action = ACTION_APPWIDGET_UPDATE
            }
            val refreshClickPendingIntent =
                PendingIntent.getBroadcast(context, 0, refreshClickIntent, 0)
            widget.setOnClickPendingIntent(R.id.iv_btn_widget_refresh, refreshClickPendingIntent)

            appWidgetManager.updateAppWidget(it, widget)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action) {
            ACTION_APPWIDGET_UPDATE -> {
                val mgr = AppWidgetManager.getInstance(context)
                val cn = ComponentName(context, WidgetProvider::class.java)
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lv_widget_plan_list)
            }

            ACTION_ITEM_CLICK -> {
                val activityIntent = Intent(context, MainActivity::class.java).apply {
                    putExtra(WIDGET_PLAN_ID, intent.getLongExtra(WIDGET_PLAN_ID, -1L))
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                context.startActivity(activityIntent)
            }
        }
    }

    companion object {
        private const val ACTION_ITEM_CLICK = "item_click"

        fun sendRefreshBroadcast(context: Context) {
            val intent = Intent(ACTION_APPWIDGET_UPDATE)
            intent.component = ComponentName(context, WidgetProvider::class.java)
            context.sendBroadcast(intent)
        }
    }
}