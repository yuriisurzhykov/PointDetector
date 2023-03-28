package com.yuriisurzhykov.foodbanks.core.data

interface Mapper<I : Any, O : Any> {

    fun map(input: I): O

    abstract class Abstract<I : Any, O : Any> : Mapper<I, O> {
        protected inline fun <reified O> exception(input: I): Exception {
            return MappingException(input, O::class)
        }
    }

    abstract class List<I : Any, O : Any> :
        Abstract<kotlin.collections.List<I>, kotlin.collections.List<O>>()

    class Empty<I : Any> : Mapper<I, Unit> {
        override fun map(input: I) = Unit
    }
}