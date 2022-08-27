package com.yuriisurzhykov.pointdetector.data.cache.configs

import com.google.gson.Gson
import com.yuriisurzhykov.pointdetector.data.repository.ConfigsRepository
import javax.inject.Inject

class SortingTypeConfig @Inject constructor(
    dataProvider: ConfigsRepository,
    gson: Gson
) : AbstractConfigService<String>(dataProvider, gson) {

    override fun getConfigName() = "points_sorting_type"

}