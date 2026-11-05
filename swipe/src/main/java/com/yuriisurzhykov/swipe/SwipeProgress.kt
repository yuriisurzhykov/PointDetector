package com.yuriisurzhykov.swipe

/**
 * Follows one drag of an item: tells when the item reaches the threshold of a side and on which
 * side the finger was released, if the item was still dragged to the threshold at that moment.
 * Knows nothing about the views, so the rule is easy to check.
 */
internal class SwipeProgress {

    private var reached: SwipeSide? = null

    fun start() {
        reached = null
    }

    /**
     * @return true when the item has just got to the [threshold], not when it is already there
     */
    fun moved(side: SwipeSide, distance: Float, threshold: Int): Boolean {
        val isReached = distance >= threshold
        val justReached = isReached && reached != side
        reached = if (isReached) side else null
        return justReached
    }

    /**
     * @return the side the item was released at, or null if it was not dragged far enough
     */
    fun released(): SwipeSide? {
        return reached.also { reached = null }
    }
}
