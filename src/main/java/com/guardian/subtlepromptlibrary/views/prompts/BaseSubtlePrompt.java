package com.guardian.subtlepromptlibrary.views.prompts;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guardian.subtlepromptlibrary.R;
import com.guardian.subtlepromptlibrary.views.SmoothHeaderRelativeLayout;

public class BaseSubtlePrompt extends SmoothHeaderRelativeLayout implements View.OnClickListener {

    private TextView titleTextView;
    private TextView bodyTextView;
    private ImageView promptImage;
    private ImageView poppingButton;
    private AnimationSet poppingButtonAnimation;
    private Drawable poppingIcon;
    private Drawable standardIcon;
    private ImageView promptCloseImageView;
    private View arrowView;
    private boolean withPoppingButton;

    public BaseSubtlePrompt(Context context) {
        super(context);
    }

    public BaseSubtlePrompt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSubtlePrompt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAnimation(Listener listener) {
        super.setAnimation(listener);
        initFields();
    }

    protected void initFields() {
        promptCloseImageView = (ImageView) findViewById(R.id.prompt_close_button);
        titleTextView = (TextView) findViewById(R.id.prompt_title_text);
        bodyTextView = (TextView) findViewById(R.id.prompt_body_text);
        promptImage = (ImageView) findViewById(R.id.prompt_image_view);
        arrowView = findViewById(R.id.arrow);
        promptCloseImageView.setOnClickListener(this);

        promptImage.setVisibility(View.GONE);
        bodyTextView.setVisibility(View.GONE);
        withPoppingButton = false;
    }

    public void setAnimation(ImageView poppingButton, Listener listener) {
        setAnimation(listener);
        this.poppingButton = poppingButton;
        withPoppingButton = true;
    }

    public void setPromptTitle(String message) {
        titleTextView.setText(message);
    }

    public void setPromptCloseButton(Drawable drawable) {
        promptCloseImageView.setBackgroundDrawable(drawable);
    }

    public void setBodyText(String body) {
        bodyTextView.setVisibility(View.VISIBLE);
        bodyTextView.setText(body);
    }

    public void setPromptImage(Drawable drawable) {
        promptImage.setVisibility(View.VISIBLE);
        promptImage.setBackgroundDrawable(drawable);
    }

    public Drawable getPoppingIconDrawable() {
        return poppingIcon;
    }

    public Drawable getStandardIconDrawable() {
        return standardIcon;
    }

    public void setPoppingIcon(Drawable poppingIcon) {
        this.poppingIcon = poppingIcon;
    }

    public void setStandardIcon(Drawable standardIcon) {
        this.standardIcon = standardIcon;
    }

    public void setArrowVisibility(int visibility) {
        arrowView.setVisibility(visibility);
     }

    public void setArrowMargins(int marginLeft, int marginTop, int marginRight, int marginBottom) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) arrowView.getLayoutParams();
        lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
    }

    @Override
    protected void customExpand() {
        animatorShow.start();
        currentState = PromptStateType.EXPANDING;
        if (withPoppingButton) {
            popUpButton();
        } else {
            setAnimationWithoutPoppingButton();
        }
    }

    private void setAnimationWithoutPoppingButton() {
        animatorShow.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentState = PromptStateType.EXPANDED;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void popUpButton() {
        if (animatorShow != null) {
            poppingButton.setImageDrawable(getPoppingIconDrawable());
            poppingButtonAnimation = new AnimationSet(false);
            Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in_out);
            poppingButtonAnimation.addAnimation(zoomIn);
            poppingButtonAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    currentState = PromptStateType.EXPANDED;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            poppingButton.startAnimation(poppingButtonAnimation);
        }
    }

    private void fadeOutButton() {
        poppingButtonAnimation = new AnimationSet(false);
        Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        poppingButtonAnimation.addAnimation(zoomIn);
        poppingButtonAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                poppingButton.setImageDrawable(getStandardIconDrawable());
                poppingButtonAnimation = new AnimationSet(false);
                Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                poppingButtonAnimation.addAnimation(zoomIn);
                poppingButton.startAnimation(poppingButtonAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        poppingButton.startAnimation(poppingButtonAnimation);
    }

    @Override
    protected void collapseOnDefaultCase() {
        super.collapseOnDefaultCase();
        if (withPoppingButton) {
            fadeOutButton();
        }
    }

    @Override
    protected void onCollapseAnimatorEnd() {
        super.onCollapseAnimatorEnd();
        if (withPoppingButton) {
            poppingButton.setImageDrawable(getStandardIconDrawable());
        }
    }

    @Override
    protected void onCollapseAnimatorStart() {
        if (withPoppingButton && poppingButtonAnimation != null && poppingButtonAnimation.isInitialized()) {
            poppingButtonAnimation.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        collapse();
    }
}
