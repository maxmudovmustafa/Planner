package com.example.myapplicatio.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.myapplicatio.R;

import uz.greenwhite.lib.view_setup.UI;

public class MyImageView extends AppCompatImageView {

    private float mCornerRadius = 0.0f;
    private Paint paint = new Paint();
    private int iconColor = 0;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyImageView, defStyleAttr, 0);

        this.mCornerRadius = a.getFloat(R.styleable.MyImageView_my_radius, 0.0f);

        final Drawable d = a.getDrawable(R.styleable.MyImageView_my_icon);
        if (d != null) {
            this.iconColor = a.getColor(R.styleable.MyImageView_my_icon_color, 0);
            d.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            setImageDrawable(d);
        }
    }

    public void setImageResource(@DrawableRes int resId, int color) {
        setImageDrawable(UI.changeDrawableColor(getContext(), resId, color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Round some corners betch!
        if (mCornerRadius > 0) {
            Path clipPath = new Path();
            RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
            clipPath.addRoundRect(rect, mCornerRadius, mCornerRadius, Path.Direction.CW);
            canvas.clipPath(clipPath);
            super.onDraw(canvas);
        } else {
            super.onDraw(canvas);
        }
    }
}
