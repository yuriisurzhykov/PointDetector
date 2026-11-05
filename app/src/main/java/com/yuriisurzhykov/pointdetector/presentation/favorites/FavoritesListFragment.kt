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
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointSwipeActions
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointsListAdapter
import com.yuriisurzhykov.pointdetector.presentation.vibration.VibrationService
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.swipe.SwipeEngine
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
        val swipes = PointSwipeActions(view.context, VibrationService(), adapter)
        SwipeEngine.Base(listOf(swipes.unfavorite { viewModel.remove(it) })).attachTo(recycler)
    }

    private val favoritesObserver = Observer<List<ViewHolderItem>> {
        adapter.submitList(it)
    }
}
