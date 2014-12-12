package com.theguardian.subtlepromptlibrary.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.theguardian.subtlepromptlibrary.R;

public class RetractableToolbar extends Toolbar {

    private ValueAnimator animatorHide;
    private int popUpHeight;
    protected ValueAnimator animatorShow;
    protected Context context;

    public RetractableToolbar(Context context) {
        super(context);
        this.context = context;
        setAnimation();
    }

    public RetractableToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setAnimation();
    }

    public RetractableToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setAnimation();
    }

    private void setAnimation() {
        popUpHeight = getDefaultPopUpHeight();
        setAnimator();
    }

    private int getDefaultPopUpHeight() {
        return context.getResources().getDimensionPixelSize(R.dimen.action_bar_height);
    }

    /*
        Hack based on the code: https://github.com/michaelworth/HeaderAnimationDemo/blob/master/App/src/main/java/com/example/headeranimationdemo/app/HeaderFrameLayout.java
        to avoid the flickering on the first frame of the prompt animation
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void setAnimator() {
        animatorShow = slideAnimator(0, popUpHeight);
        animatorHide = collapseAnimator(popUpHeight);
    }

    public void setPopUpHeight(int popUpHeight) {
        this.popUpHeight = popUpHeight;
    }

    public void show() {
        if (animatorShow == null) {
            setAnimator();
        }
        animatorShow.start();
    }

    public void hide() {
        animatorHide.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                if (getVisibility() != View.VISIBLE) {
                    setVisibility(View.VISIBLE);
                }
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = value;
                setLayoutParams(layoutParams);
            }
        });

        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private ValueAnimator collapseAnimator(int end) {
        return slideAnimator(end, 0);
    }
}
