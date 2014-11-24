package com.guardian.subtlepromptlibrary.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.guardian.subtlepromptlibrary.R;

public class SmoothHeaderRelativeLayout extends RelativeLayout {
    private static final int DELAY_SHOW_POPUP = 500;

    private ValueAnimator animatorHide;
    private Listener listener;
    private int popUpHeight;
    protected ValueAnimator animatorShow;
    protected PromptStateType currentState;
    protected Context context;

    public SmoothHeaderRelativeLayout(Context context) {
        super(context);
        this.context = context;
        popUpHeight = getDefaultPopUpHeight();
    }

    public SmoothHeaderRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        popUpHeight = getDefaultPopUpHeight();
    }

    public SmoothHeaderRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        popUpHeight = getDefaultPopUpHeight();
    }

    private int getDefaultPopUpHeight() {
        return context.getResources().getDimensionPixelSize(R.dimen.action_bar_height) + getStatusBarHeight();
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

    protected enum PromptStateType {
        COLLAPSED, COLLAPSING, EXPANDING, EXPANDED
    }

    public interface Listener {
        public void onCollapseEnd();
    }

    private int getStatusBarHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            return 0; //The status bar hiding seems to behave differently on ICS

        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? res.getDimensionPixelSize(resourceId) : 0;
    }

    public void setAnimation(Listener listener) {
        this.listener = listener;
        currentState = PromptStateType.COLLAPSED;
        setAnimator();
    }

    public boolean isExpanded() {
        return currentState == PromptStateType.EXPANDED;
    }

    private void setAnimator() {
        animatorShow = slideAnimator(0, popUpHeight);
        animatorHide = collapseAnimator(popUpHeight);
    }

    public void setPopUpHeight(int popUpHeight) {
        this.popUpHeight = popUpHeight;
    }

    public void expandWithoutAnimation() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = popUpHeight;
        setLayoutParams(layoutParams);
    }

    public void expandWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                expand();
            }
        }, DELAY_SHOW_POPUP);
    }

    private void expand() {
        if (animatorShow == null) {
            setAnimator();
        }
        customExpand();
    }

    protected void customExpand() {
        animatorShow.start();
        currentState = PromptStateType.EXPANDING;
    }

    public void collapse() {
        switch (currentState) {
            case COLLAPSED:
                break;
            case EXPANDING:
                animatorShow.cancel();
            default:
                collapseOnDefaultCase();
        }
    }

    protected void collapseOnDefaultCase() {
        currentState = PromptStateType.COLLAPSING;
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
        ValueAnimator animatorHide = slideAnimator(end, 0);
        animatorHide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                onCollapseAnimatorEnd();
            }

            @Override
            public void onAnimationStart(Animator animator) {
                onCollapseAnimatorStart();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        return animatorHide;
    }

    protected void onCollapseAnimatorStart() {

    }

    protected void onCollapseAnimatorEnd() {
        currentState = PromptStateType.COLLAPSED;
        listener.onCollapseEnd();
    }
}
