package com.yuriisurzhykov.pointdetector.uicomponents.list

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.uicomponents.FactoryContext
import com.yuriisurzhykov.pointdetector.uicomponents.R

class LayoutManagerFactory : FactoryContext<RecyclerView.LayoutManager> {
    override fun produce(context: Context): RecyclerView.LayoutManager {
        return if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, context.resources.getInteger(R.integer.landscape_span_column_count))
        } else {
            LinearLayoutManager(context)
        }
    }
}