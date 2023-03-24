package com.yuriisurzhykov.foodbanks.core

fun <T : Any, O : Any> T.map(mapper: Mapper<T, O>): O {
    return mapper.map(this)
}