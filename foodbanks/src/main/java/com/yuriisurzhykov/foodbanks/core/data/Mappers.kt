package com.yuriisurzhykov.foodbanks.core

import com.yuriisurzhykov.foodbanks.core.data.Mapper

fun <T : Any, O : Any> T.map(mapper: Mapper<T, O>): O {
    return mapper.map(this)
}