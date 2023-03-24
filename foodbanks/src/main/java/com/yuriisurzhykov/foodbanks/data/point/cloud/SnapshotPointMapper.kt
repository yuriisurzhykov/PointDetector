package com.yuriisurzhykov.foodbanks.data.point.cloud

import com.google.firebase.database.DataSnapshot
import com.yuriisurzhykov.foodbanks.core.FirebaseTypeIndicator
import com.yuriisurzhykov.foodbanks.core.Mapper

interface SnapshotPointMapper : Mapper<DataSnapshot, PointCloud> {

    abstract class Abstract : Mapper.Abstract<DataSnapshot, PointCloud>(), SnapshotPointMapper {
        override fun map(input: DataSnapshot): PointCloud {
            return input.getValue(FirebaseTypeIndicator<PointCloud>())
                ?: throw exception<PointCloud>(input)
        }
    }

    class Base : Abstract()
}