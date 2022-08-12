package com.yuriisurzhykov.pointdetector.data.repository

import com.yuriisurzhykov.pointdetector.core.ConditionFetch
import com.yuriisurzhykov.pointdetector.core.Delete
import com.yuriisurzhykov.pointdetector.core.Fetch
import com.yuriisurzhykov.pointdetector.core.Save
import kotlinx.coroutines.flow.Flow

interface CrudRepository<E> : Save<E>, Fetch<Flow<List<E>>>, ConditionFetch<String, Flow<List<E>>>, Delete<E>