package com.yuriisurzhykov.foodbanks.core.domain

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.core.data.RepositoryResponse

interface UseCaseMapper<I : Any, O : Any> : Mapper<RepositoryResponse<I>, UseCaseResponse<O>> {

    abstract class Abstract<I : Any, O : Any> : UseCaseMapper<I, O>,
        Mapper.Abstract<RepositoryResponse<I>, UseCaseResponse<O>>()
}