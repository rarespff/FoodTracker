package com.example.licenta2024.page.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.lzyzsd.circleprogress.DonutProgress;

public class CaloriesProgress extends DonutProgress {

    public CaloriesProgress(Context context) {
        super(context);
    }

    public CaloriesProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CaloriesProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void initByAttributes(TypedArray attributes) {
        super.initByAttributes(attributes);
        // Change suffix text to "kcal"
        setSuffixText(" kCal");
    }
}

