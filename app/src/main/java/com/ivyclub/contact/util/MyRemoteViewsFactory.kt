package com.ivyclub.contact.util

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.ivyclub.data.model.SimplePlanData
import java.util.Date
import com.ivyclub.contact.R
import com.ivyclub.data.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRemoteViewsFactory(
    private val context: Context,
    private val repository: ContactRepository
) :
    RemoteViewsService.RemoteViewsFactory {

    private var data = listOf<SimplePlanData>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            data = repository.getPlanList().filter { it.date > Date() }.sortedBy { it.date }
        }
    }

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        CoroutineScope(Dispatchers.IO).launch {
            data = repository.getPlanList().filter { it.date > Date() }.sortedBy { it.date }
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val listviewWidget = RemoteViews(context.packageName, R.layout.item_widget)
        listviewWidget.setTextViewText(
            R.id.tv_widget_plan_date,
            data[position].date.toString()
        )
        listviewWidget.setTextViewText(R.id.tv_widget_plan_title, data[position].title)
        return listviewWidget
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}