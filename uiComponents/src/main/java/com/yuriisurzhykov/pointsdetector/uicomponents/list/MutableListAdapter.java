package com.yuriisurzhykov.pointsdetector.uicomponents.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface MutableListAdapter<T> {

    void submitList(@Nullable List<T> list);

    void removeItem(@NonNull T item);

    void removeItem(int position);

    void insertItem(@NonNull T item, int position);

    @NonNull
    List<T> getCurrentList();

}
