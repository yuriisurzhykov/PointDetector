package com.yuriisurzhykov.foodbanks.core.presentation

import kotlinx.coroutines.*
import javax.inject.Inject

interface Dispatchers {

    fun launchUi(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit): Job

    fun launchBackground(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit): Job

    suspend fun changeToUi(block: suspend CoroutineScope.() -> Unit)

    abstract class Abstract(
        private val uiDispatcher: CoroutineDispatcher,
        private val backgroundDispatcher: CoroutineDispatcher
    ) : Dispatchers {
        override fun launchUi(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit): Job {
            return scope.launch(uiDispatcher, block = block)
        }

        override fun launchBackground(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit): Job {
            return scope.launch(backgroundDispatcher, block = block)
        }

        override suspend fun changeToUi(block: suspend CoroutineScope.() -> Unit) {
            withContext(uiDispatcher, block = block)
        }
    }

    open class Base @Inject constructor() :
        Abstract(kotlinx.coroutines.Dispatchers.Main, kotlinx.coroutines.Dispatchers.IO)

}