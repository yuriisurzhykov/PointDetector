package com.yuriisurzhykov.pointdetector.data.cache.migrations

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

/**
 * The points moved to the cities database of the foodbanks module. The points
 * saved here, the ones the users added and their favorites, are not migrated.
 * The configs of the app stay untouched.
 */
@DeleteTable(tableName = "PointCache")
class MigrationV3ToV4Spec : AutoMigrationSpec
