package com.yuriisurzhykov.pointdetector.presentation.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractStyleFragment
import com.yuriisurzhykov.pointdetector.presentation.delegate.findView
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointsListAdapter
import com.yuriisurzhykov.pointsdetector.uicomponents.list.SwipeRecyclerCallbacks
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesListFragment : AbstractStyleFragment(R.layout.fragment_favorite_list) {

    private val viewModel: FavoriteListViewModel by viewModels()

    private val recycler: RecyclerView by findView(R.id.recycler)
    private val adapter = PointsListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(viewLifecycleOwner, favoritesObserver)
        recycler.layoutManager = LinearLayoutManager(view.context)
        recycler.adapter = adapter
        adapter.setOnSwipeListener(object : SwipeRecyclerCallbacks<ViewHolderItem> {
            override fun onSwipedToLeft(viewHolder: RecyclerView.ViewHolder, position: Int, item: ViewHolderItem) {
                super.onSwipedToLeft(viewHolder, position, item)
                (item as? PointUi)?.let { viewModel.remove(it) }
            }
        })
    }

    private val favoritesObserver = Observer<List<ViewHolderItem>> {
        adapter.submitList(it)
    }
}