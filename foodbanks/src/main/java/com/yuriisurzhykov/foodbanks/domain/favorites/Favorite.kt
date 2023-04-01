package com.yuriisurzhykov.foodbanks.domain.favorites

data class Favorite(
    val id: Long,
    private val pointId: Long,
    private val name: String,
    private val address: String
)