package com.yuriisurzhykov.pointsdetector.uicomponents.list

interface ViewHolderItem {

    fun areItemsTheSame(other: Any): Boolean
    fun areContentsTheSame(other: Any): Boolean

    abstract class Abstract : ViewHolderItem {
        override fun areItemsTheSame(other: Any): Boolean {
            return other == this
        }
    }
}