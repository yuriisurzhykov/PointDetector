package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.DataSnapshot
import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.core.data.SnapshotValue
import com.yuriisurzhykov.foodbanks.core.data.map
import javax.inject.Inject

interface SnapshotCityMapper : Mapper<DataSnapshot, CityCloud> {

    abstract class Abstract(
        private val valueMapper: ValueCityMapper
    ) : SnapshotCityMapper, Mapper.Abstract<DataSnapshot, CityCloud>() {

        override fun map(input: DataSnapshot): CityCloud {
            val value = input.value as? Map<*, *> ?: throw exception<CityCloud>(input)
            return SnapshotValue.Base(value).map(valueMapper)
        }
    }

    class Base @Inject constructor(valueMapper: ValueCityMapper) : Abstract(valueMapper)
}
