package com.yuriisurzhykov.pointdetector.presentation.favorites

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesFetchUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesRemoveUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.list.EmptyStateData
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val favoritesUseCase: FavoritesFetchUseCase,
    private val favRemoveUseCase: FavoritesRemoveUseCase
) : ViewModel(), FavoritesRemove<PointUi> {

    private val liveData = MutableLiveData<List<ViewHolderItem>>()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        dispatchers.launchBackground(viewModelScope) {
            favoritesUseCase.fetchFavorites().onEach { postResult(it) }.collect()
        }
    }

    private fun postResult(it: List<PointUi>?) {
        if (it.isNullOrEmpty()) {
            liveData.postValue(listOf(EmptyStateData()))
        } else {
            liveData.postValue(it.toList())
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<List<ViewHolderItem>>) {
        liveData.observe(owner, observer)
    }

    /**
     * The list is loaded again, so it shows the empty state when the last favorite is removed.
     */
    override fun remove(item: PointUi) {
        dispatchers.launchBackground(viewModelScope) {
            favRemoveUseCase.remove(item)
            loadFavorites()
        }
    }
}
