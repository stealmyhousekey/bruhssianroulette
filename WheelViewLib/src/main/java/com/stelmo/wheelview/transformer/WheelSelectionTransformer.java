package com.stelmo.wheelview.transformer;

import android.graphics.drawable.Drawable;

import com.stelmo.wheelview.WheelView;

public interface WheelSelectionTransformer {
    void transform(Drawable drawable, WheelView.ItemState itemState);
}
