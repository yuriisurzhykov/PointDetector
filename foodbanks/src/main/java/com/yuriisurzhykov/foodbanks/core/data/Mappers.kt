package com.yuriisurzhykov.foodbanks.core.data

fun <T : Any, O : Any> T.map(mapper: Mapper<T, O>): O {
    return mapper.map(this)
}