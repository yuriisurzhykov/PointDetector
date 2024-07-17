package com.yuriisurzhykov.pointdetector.uicomponents.list;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface ViewHolderTypeManager<T> {

    int getViewHolderType(@NonNull T item);

    @NonNull
    Class<? extends AbstractViewHolder<T>> getViewHolderClass(int viewType);

    abstract class Abstract<T> implements ViewHolderTypeManager<T> {

        private final Map<Integer, Class<? extends AbstractViewHolder<T>>> typeMap = new HashMap<>();

        public abstract Pair<Integer, Class<? extends AbstractViewHolder<T>>> getViewHolderTypeWithClass(@NonNull T item);

        @Override
        public int getViewHolderType(@NonNull T item) {
            Pair<Integer, Class<? extends AbstractViewHolder<T>>> pair = getViewHolderTypeWithClass(item);
            typeMap.put(pair.first, pair.second);
            return pair.first;
        }

        @Override
        @NonNull
        public Class<? extends AbstractViewHolder<T>> getViewHolderClass(int viewType) {
            return Objects.requireNonNull(typeMap.get(viewType));
        }
    }
}
