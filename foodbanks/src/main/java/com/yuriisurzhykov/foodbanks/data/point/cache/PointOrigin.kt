package com.yuriisurzhykov.foodbanks.data.point.cache

/**
 * Where a point comes from. Points of different origins live side by side, but only
 * the cloud points are managed by the cloud sync.
 */
enum class PointOrigin {
    CLOUD,
    USER
}
