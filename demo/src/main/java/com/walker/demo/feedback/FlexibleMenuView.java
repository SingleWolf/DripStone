package com.walker.demo.feedback;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.walker.demo.R;


public class FlexibleMenuView extends ViewGroup implements View.OnClickListener {

    private static final String TAG_BG_OPEN = "bg_open";
    private static final String TAG_BG_CLOSE = "bg_close";
    private int mRadius;
    private Status mStatus = Status.CLOSE;
    //主菜的单按钮
    private View mCButton;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private OnMenuShowStateListener onMenuShowStateListener;
    private View mBgCloseView = null;

    /**
     * 菜单的状态枚举类
     */
    public enum Status {
        OPEN, CLOSE
    }

    /**
     * 点击子菜单项的回调接口
     */
    public interface OnMenuItemClickListener {
        void onClick(View view, int pos);
    }

    public interface OnMenuShowStateListener {
        void onOpen(boolean isOpen);
    }

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnMenuShowStateListener(OnMenuShowStateListener showStateListener) {
        this.onMenuShowStateListener = showStateListener;
    }

    public FlexibleMenuView(Context context) {
        this(context, null);
    }

    public FlexibleMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FlexibleMenuView, defStyle, 0);
        mRadius = (int) a.getDimension(R.styleable.FlexibleMenuView_max_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                        getResources().getDisplayMetrics()));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCButton();
            layoutMenuItems();
        }
    }

    @Override
    public void onClick(View view) {
        toggleMenu(300);
    }

    public void autoOpen() {
        if (isOpen() == false) {
            toggleMenu(300);
        }
    }

    /**
     * 布局主菜单项
     */
    private void layoutCButton() {
        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);
        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();
        int l = (getMeasuredWidth() - width) / 2;
        int t = getMeasuredHeight() - height;
        mCButton.layout(l, t, l + width, t + height);

        mBgCloseView = mCButton.findViewWithTag(TAG_BG_CLOSE);
    }

    /**
     * 布局子菜单项
     */
    private void layoutMenuItems() {
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            View child = getChildAt(i + 1);
            int l = 0;
            int t = (mRadius / (count - 1) * (i + 1));
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            t = getMeasuredHeight() - height - t;
            l = (getMeasuredWidth() - width) / 2;
            child.layout(l, t, l + width, t + height);
            child.setVisibility(View.GONE);
        }
    }

    /**
     * 切换菜单
     */
    public void toggleMenu(int duration) {
        // 为menuItem添加平移动画和旋转动画
        int count = getChildCount();

        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);

            // end 0 , 0
            // start
            int cl = 0;
            int ct = (mRadius / (count - 1) * (i + 1));

            AnimationSet animset = new AnimationSet(true);
            Animation tranAnim = null;

            // to open
            if (mStatus == Status.CLOSE) {
                tranAnim = new TranslateAnimation(0, cl, ct, 0);
                childView.setClickable(true);
                childView.setFocusable(true);

            } else
            // to close
            {
                tranAnim = new TranslateAnimation(cl, 0, 0, ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            tranAnim.setStartOffset(i * 200);

            tranAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mStatus == Status.CLOSE) {
                        childView.setVisibility(View.GONE);
                    }
                }
            });

            animset.addAnimation(tranAnim);
            childView.startAnimation(animset);

            final int pos = i + 1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.onClick(childView, pos);
                    }

                    menuItemAnim(pos - 1);
                    changeStatus();

                }
            });
        }
        // 切换菜单状态
        changeStatus();

    }

    /**
     * 添加menuItem的点击动画
     */
    private void menuItemAnim(int pos) {
        for (int i = 0; i < getChildCount() - 1; i++) {

            View childView = getChildAt(i + 1);
            if (i == pos) {
                childView.startAnimation(scaleBigAnim(300));
            } else {

                childView.startAnimation(scaleSmallAnim(300));
            }

            childView.setClickable(false);
            childView.setFocusable(false);

        }

    }

    /**
     * 为当前点击的Item设置变小和透明度增大的动画
     *
     * @param duration
     * @return
     */
    private Animation scaleSmallAnim(int duration) {

        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;

    }

    /**
     * 为当前点击的Item设置变大和透明度降低的动画
     */
    private Animation scaleBigAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;

    }

    /**
     * 切换菜单状态
     */
    private void changeStatus() {
        mStatus = (mStatus == Status.CLOSE ? Status.OPEN
                : Status.CLOSE);
        updateBackgroud();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onMenuShowStateListener != null) {
                    onMenuShowStateListener.onOpen(isOpen());
                }
            }
        }, 500);
    }

    /**
     * 是否处于展开状态
     *
     * @return
     */
    public boolean isOpen() {
        return mStatus == Status.OPEN;
    }

    private void updateBackgroud() {
        if (isOpen()) {
            if (mBgCloseView != null) {
                mBgCloseView.setVisibility(View.GONE);
            }
        } else {
            if (mBgCloseView != null) {
                mBgCloseView.setVisibility(View.VISIBLE);
            }
        }
    }
}
