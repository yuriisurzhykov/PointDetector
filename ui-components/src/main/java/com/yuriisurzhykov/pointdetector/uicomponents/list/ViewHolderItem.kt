package com.yuriisurzhykov.pointdetector.uicomponents.list

interface ViewHolderItem {

    fun areItemsTheSame(other: Any): Boolean
    fun areContentsTheSame(other: Any): Boolean

    abstract class Abstract : ViewHolderItem {
        override fun areItemsTheSame(other: Any): Boolean {
            return other == this
        }
    }
}