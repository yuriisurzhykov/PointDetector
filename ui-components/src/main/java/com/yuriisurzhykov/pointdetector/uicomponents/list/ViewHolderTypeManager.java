package com.yuriisurzhykov.pointdetector.uicomponents.list;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface ViewHolderTypeManager<T> {

    int getViewHolderType(@NonNull T item);

    @NonNull
    ViewHolderContainer<T> getViewHolderClass(int viewType);

    abstract class Abstract<T> implements ViewHolderTypeManager<T> {

        private final Map<Integer, ViewHolderContainer<T>> typeMap = new HashMap<>();

        public abstract ViewHolderContainer<T> getViewHolderTypeWithClass(@NonNull T item);

        @Override
        public int getViewHolderType(@NonNull T item) {
            ViewHolderContainer<T> pair = getViewHolderTypeWithClass(item);
            typeMap.put(pair.getType(), pair);
            return pair.getType();
        }

        @Override
        @NonNull
        public ViewHolderContainer<T> getViewHolderClass(int viewType) {
            return Objects.requireNonNull(typeMap.get(viewType));
        }
    }
}
