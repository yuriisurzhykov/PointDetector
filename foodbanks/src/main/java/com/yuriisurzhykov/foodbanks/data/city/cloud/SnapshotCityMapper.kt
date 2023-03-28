package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.DataSnapshot
import com.yuriisurzhykov.foodbanks.core.FirebaseTypeIndicator
import com.yuriisurzhykov.foodbanks.core.data.Mapper
import javax.inject.Inject

interface SnapshotCityMapper : Mapper<DataSnapshot, CityCloud> {

    abstract class Abstract : SnapshotCityMapper, Mapper.Abstract<DataSnapshot, CityCloud>() {
        override fun map(input: DataSnapshot): CityCloud {
            return input.getValue(FirebaseTypeIndicator<CityCloud>())
                ?: throw exception<CityCloud>(input)
        }
    }

    class Base @Inject constructor() : Abstract()
}