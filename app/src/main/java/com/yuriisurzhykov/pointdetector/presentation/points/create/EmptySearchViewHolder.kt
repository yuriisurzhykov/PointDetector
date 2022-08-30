package com.yuriisurzhykov.pointdetector.presentation.points.create

import android.view.View
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.points.create.EmptySearchData
import com.yuriisurzhykov.pointsdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointsdetector.uicomponents.list.OnItemClickListener
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolder

@ViewHolder(R.layout.list_item_entered_data_empty)
class EmptySearchViewHolder(view: View) : AbstractViewHolder<EmptySearchData>(view) {

    private val text: TextView by lazy { itemView.findViewById(android.R.id.text1) }

    override fun bind(item: EmptySearchData, clickListener: OnItemClickListener<EmptySearchData>?) {
        super.bind(item, clickListener)
        text.setText(R.string.label_empty_search_data)
    }
}