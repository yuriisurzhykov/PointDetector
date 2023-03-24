package com.yuriisurzhykov.foodbanks.core

interface Mapper<I : Any, O : Any> {



    abstract class Abstract<I : Any, O : Any> : Mapper<I, O> {
        protected inline fun <reified O> exception(input: I): Exception {
            return MappingException(input, O::class)
        }
    }

    class Empty<I : Any> : Mapper<I, Unit> {
        override fun map(input: I) = Unit
    }
}