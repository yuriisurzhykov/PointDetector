package com.yuriisurzhykov.pointdetector.data.cache.migrations

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn(tableName = "ConfigEntity", columnName = "id")
class MigrationV1ToV2Spec: AutoMigrationSpec