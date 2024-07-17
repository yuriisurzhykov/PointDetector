package com.yuriisurzhykov.pointdetector.presentation.list

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointdetector.uicomponents.list.OnItemClickListener
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolder
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem

class StartSearchData : ViewHolderItem.Abstract() {

    override fun areContentsTheSame(other: Any): Boolean {
        return areItemsTheSame(other)
    }
}

@ViewHolder(R.layout.list_item_empty_points_list)
class StartSearchViewHolder(view: View) : AbstractViewHolder<StartSearchData>(view) {

    private val image: ImageView by lazy { itemView.findViewById(android.R.id.icon) }
    private val title: TextView by lazy { itemView.findViewById(android.R.id.text1) }
    private val description: TextView by lazy { itemView.findViewById(android.R.id.text2) }
    private val button: Button by lazy { itemView.findViewById(android.R.id.button1) }

    override fun bind(item: StartSearchData, clickListener: OnItemClickListener<StartSearchData>?) {
        super.bind(item, clickListener)
        button.visibility = View.GONE
        image.setImageResource(R.drawable.ic_empty_state)
        title.visibility = View.GONE
        description.setText(R.string.label_description_search_address)
    }
}