package com.yuriisurzhykov.swipe

/**
 * What happens with the item after the swipe is done.
 */
enum class SwipeBehavior {

    /**
     * The item is let go and goes back to its place, the action is done when the finger is
     * released after the item was dragged to the threshold. Fits the actions that change the
     * item, for example a favorite.
     */
    RETURN,

    /**
     * The item leaves the list, the action is done when the item is swiped out. The action must
     * remove the item from the adapter, otherwise the item stays swiped out. Fits the actions
     * that remove the item, for example a delete.
     */
    DISMISS
}
