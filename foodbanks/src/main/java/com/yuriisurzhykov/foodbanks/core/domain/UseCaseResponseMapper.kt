package com.yuriisurzhykov.foodbanks.core.domain

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.core.data.RepositoryResponse
import com.yuriisurzhykov.foodbanks.core.data.map

interface UseCaseResponseMapper<I : Any, O : Any> :
    Mapper<RepositoryResponse<I>, UseCaseResponse<O>> {

    interface List<I : Any, O : Any> :
        UseCaseResponseMapper<kotlin.collections.List<I>, kotlin.collections.List<O>>

    abstract class AbstractList<I : Any, O : Any>(
        mapper: Mapper<kotlin.collections.List<I>, kotlin.collections.List<O>>
    ) : Abstract<kotlin.collections.List<I>, kotlin.collections.List<O>>(mapper), List<I, O>

    abstract class Abstract<I : Any, O : Any>(
        private val mapper: Mapper<I, O>
    ) : UseCaseResponseMapper<I, O>,
        Mapper.Abstract<RepositoryResponse<I>, UseCaseResponse<O>>() {

        override fun map(input: RepositoryResponse<I>): UseCaseResponse<O> {
            when (input) {
                is RepositoryResponse.Success -> return UseCaseResponse.Success(
                    input.items.map(mapper)
                )
                is RepositoryResponse.NoNetwork -> return UseCaseResponse.NoNetwork(
                    input.cacheItems.map(mapper)
                )
                is RepositoryResponse.NetworkError -> return UseCaseResponse.NetworkError()
            }
            throw exception<UseCaseResponse<*>>(input)
        }
    }
}