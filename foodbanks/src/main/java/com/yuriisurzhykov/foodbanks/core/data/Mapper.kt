package com.yuriisurzhykov.foodbanks.core.data

interface Mapper<I : Any, O : Any> {

    fun map(input: I): O

    abstract class Abstract<I : Any, O : Any> : Mapper<I, O> {
        protected inline fun <reified O> exception(input: I): Exception {
            return MappingException(input, O::class)
        }
    }

    interface List<I : Any, O : Any> :
        Mapper<kotlin.collections.List<I>, kotlin.collections.List<O>> {
        fun mapSingle(input: I): O
    }

    abstract class AbstractList<I : Any, O : Any> : List<I, O>,
        Abstract<kotlin.collections.List<I>, kotlin.collections.List<O>>() {
        override fun map(input: kotlin.collections.List<I>): kotlin.collections.List<O> {
            return input.map { mapSingle(it) }
        }
    }

    class Empty<I : Any> : Mapper<I, Unit> {
        override fun map(input: I) = Unit
    }
}