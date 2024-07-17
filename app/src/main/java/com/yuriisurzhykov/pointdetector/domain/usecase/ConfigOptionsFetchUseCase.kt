package com.yuriisurzhykov.pointdetector.domain.usecase

import android.content.Context
import androidx.annotation.XmlRes
import com.yuriisurzhykov.pointdetector.data.cache.configs.AbstractConfigService
import com.yuriisurzhykov.pointdetector.presentation.core.getHashMapResource
import com.yuriisurzhykov.pointdetector.uicomponents.textselect.SelectOption

interface ConfigOptionsFetchUseCase {

    suspend fun fetch(): List<SelectOption>

    abstract class Abstract constructor(
        private val context: Context,
        private val configService: AbstractConfigService<String>,
        @XmlRes private val arrayId: Int
    ) : ConfigOptionsFetchUseCase {

        override suspend fun fetch(): List<SelectOption> {
            val arrayOptions = context.resources.getHashMapResource(arrayId)
            val selectedOption = configService.getConfigValue()
            val result =
                arrayOptions?.map { SelectOption(it.key, it.value, selectedOption == it.key) }
            return result.orEmpty()
        }
    }

}