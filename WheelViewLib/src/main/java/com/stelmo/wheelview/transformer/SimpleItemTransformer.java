package com.stelmo.wheelview.transformer;

import android.graphics.Rect;

import com.stelmo.wheelview.Circle;
import com.stelmo.wheelview.WheelView;

public class SimpleItemTransformer implements WheelItemTransformer {
    @Override
    public void transform(WheelView.ItemState itemState, Rect itemBounds) {
        Circle bounds = itemState.getBounds();
        float radius = bounds.getRadius();
        float x = bounds.getCenterX();
        float y = bounds.getCenterY();
        itemBounds.set(Math.round(x - radius), Math.round(y - radius), Math.round(x + radius), Math.round(y + radius));
    }
}
