package com.yuriisurzhykov.pointdetector.domain.messages

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val BUFFER = 8

/**
 * The one stream of messages for the person. Any layer posts a fact ([AppMessage]); the screen
 * that is in front shows it. The same message is not posted again while it is "quiet", so a screen
 * that reloads often (an offline phone asks the network at every start) does not repeat itself.
 */
@Singleton
class AppMessages internal constructor(private val now: () -> Long) {

    @Inject
    constructor() : this(System::currentTimeMillis)

    private val mutableEvents = MutableSharedFlow<AppMessage>(extraBufferCapacity = BUFFER)
    val events: SharedFlow<AppMessage> = mutableEvents.asSharedFlow()

    private val lastPosted = mutableMapOf<AppMessage, Long>()

    fun post(message: AppMessage) {
        val time = now()
        val posted = synchronized(lastPosted) {
            val last = lastPosted[message]
            if (last != null && time - last < message.quietMillis) {
                false
            } else {
                lastPosted[message] = time
                true
            }
        }
        if (posted) mutableEvents.tryEmit(message)
    }
}
