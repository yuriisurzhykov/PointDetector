package com.yuriisurzhykov.pointdetector.domain.messages

/** Tells the person that the heart of a place was switched: [wasFavorite] is the state before the tap. */
fun AppMessages.postFavoriteChange(wasFavorite: Boolean) {
    post(if (wasFavorite) AppMessage.FavoriteRemoved else AppMessage.FavoriteAdded)
}
