package com.stelmo.wheelview.transformer;

import android.graphics.Rect;

import com.stelmo.wheelview.WheelView;

public interface WheelItemTransformer {
    /**
     * You have control over the Items draw bounds. By supplying your own WheelItemTransformer
     * you must call set bounds on the itemBounds.
     */
    void transform(WheelView.ItemState itemState, Rect itemBounds);
}
