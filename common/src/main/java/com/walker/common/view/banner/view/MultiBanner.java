package com.walker.common.view.banner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewpager.widget.ViewPager;

import com.walker.common.R;
import com.walker.common.view.ViewPagerScroller;
import com.walker.common.view.banner.adapter.MultiPagerAdapter;
import com.walker.common.view.banner.holder.HolderCreator;
import com.walker.common.view.banner.holder.ViewHolder;
import com.walker.core.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Walker
 * @date on 2018/5/17 0017 下午 15:02
 * @email feitianwumu@163.com
 * @desc 多功能Banner
 */
public class MultiBanner<T> extends FrameLayout implements LifecycleObserver {
    /**
     * ViewPager
     */
    private ViewPager mViewPager;
    //  轮播数据集合
    private List<T> mList;
    //  重新构造后的轮播数据集合
    private List<T> mListAdd;
    //  指示器图片集合
    private List<DotView> mDotList;
    //  图片切换时间间隔
    private int interval;
    //  圆点位置
    private int dotPosition = 0;
    //  图片上一个位置
    private int prePosition = 0;
    //  图片当前位置
    private int currentPosition;
    //  是否正在循环
    private boolean isLooping;
    //  是否开启循环
    private boolean isCanLoop;
    //  是否开启自动播放
    private boolean isAutoPlay = false;
    //  是否显示指示器圆点
    private boolean showIndicator = true;
    // 圆点指示器显示位置
    public static final int START = 1;
    public static final int END = 2;
    public static final int CENTER = 0;
    // 滑动速度
    private int mScrollDuration = 2000;

    private int gravity;
    //  未选中时圆点颜色
    private int indicatorNormalColor;
    //  选中时选点颜色
    private int indicatorCheckedColor;
    //  指示器圆点半径
    private float indicatorRadius;
    //  页面点击事件监听
    private OnPageClickListener mOnPageClickListener;
    //  圆点指示器的Layout
    private LinearLayout mLlIndicator;
    private HolderCreator holderCreator;
    private MultiPagerAdapter<T> adapter;

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager.getChildCount() > 1) {
                mHandler.postDelayed(this, interval);
                currentPosition++;
                mViewPager.setCurrentItem(currentPosition, true);
            }
        }
    };

    public MultiBanner(Context context) {
        super(context);
        init(null, context);
    }

    public MultiBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(attrs, context);
    }

    public MultiBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            initData();
            setIndicator();
            setViewPager();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(1600, 540);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(1600, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 540);
        }
    }

    private void init(AttributeSet attrs, Context context) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiBanner);
            interval = typedArray.getInteger(R.styleable.MultiBanner_interval, 3000);
            indicatorCheckedColor = typedArray.getColor(R.styleable.MultiBanner_indicator_checked_color, Color.parseColor("#FF4C39"));
            indicatorNormalColor = typedArray.getColor(R.styleable.MultiBanner_indicator_normal_color, Color.parseColor("#935656"));
            indicatorRadius = typedArray.getDimension(R.styleable.MultiBanner_indicator_radius, DisplayUtils.dp2px(context, 4));
            isAutoPlay = typedArray.getBoolean(R.styleable.MultiBanner_isAutoPlay, true);
            isCanLoop = typedArray.getBoolean(R.styleable.MultiBanner_isCanLoop, true);
            gravity = typedArray.getInt(R.styleable.MultiBanner_indicator_gravity, 0);
            typedArray.recycle();
        }

        mList = new ArrayList<>();
        mListAdd = new ArrayList<>();
        mDotList = new ArrayList<>();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.common_layout_banner_default, this);
        mLlIndicator = view.findViewById(R.id.layoutIndicator);
        mViewPager = view.findViewById(R.id.vpBanner);
        mViewPager.setClipChildren(false);
    }

    /**
     * 设置ViewPager布局
     *
     * @param layoutId    布局资源id
     * @param viewPagerId viewpager资源id
     * @param indicatorId 指示器资源id
     */
    public void setViewPagerLayout(int layoutId, int viewPagerId, int indicatorId) {
        try {
            View view = LayoutInflater.from(getContext()).inflate(layoutId, this);
            mLlIndicator = view.findViewById(indicatorId);
            mViewPager = view.findViewById(viewPagerId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ViewPager'layout is valid !");
        }
    }

    /**
     * 设置banner展示位的布局参数
     *
     * @param width  宽
     * @param height 高
     */
    public void setContentLayoutParams(int width, int height) {
        if (mViewPager == null) {
            return;
        }

        if (0 < width) {
            ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.width = width;
            mViewPager.setLayoutParams(params);
        }

        if (0 < height) {
            ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.height = height;
            mViewPager.setLayoutParams(params);
        }
    }

    /**
     * 设置缓存的页面数量
     *
     * @param value 缓存的页面数量
     */
    public void setOffscreenPageLimit(int value) {
        //设置缓存的页面数量
        if (mViewPager != null) {
            mViewPager.setOffscreenPageLimit(value);
        }
    }

    /**
     * 设置Page间间距
     *
     * @param marginPixels 间距
     */
    public void setPageMargin(int marginPixels) {
        //设置Page间间距
        if (mViewPager != null) {
            mViewPager.setPageMargin(marginPixels);
        }
    }

    /**
     * 设置滑动速度
     *
     * @param duration
     */
    public void setScrollDuration(int duration) {
        mScrollDuration = duration;
    }

    private void setIndicatorGravity() {
        switch (gravity) {
            case CENTER:
                mLlIndicator.setGravity(Gravity.CENTER);
                break;
            case START:
                mLlIndicator.setGravity(Gravity.START);
                break;
            case END:
                mLlIndicator.setGravity(Gravity.END);
                break;
        }
    }

    //  根据mList数据集构造mListAdd
    private void initData() {
        if (mList.size() == 0) {
            //setVisibility(GONE);
            setVisibility(VISIBLE);
        } else if (mList.size() == 1) {
            mListAdd.add(mList.get(0));
            setVisibility(VISIBLE);
        } else if (mList.size() > 1) {
            createData();
            setVisibility(VISIBLE);
        }
    }

    private void updateData(List<T> list) {
        mList.clear();
        mList.addAll(list);

        if (mList.size() == 0) {
            setVisibility(VISIBLE);
        } else if (mList.size() == 1) {
            mListAdd.clear();
            mListAdd.add(mList.get(0));
            setVisibility(VISIBLE);
        } else if (mList.size() > 1) {
            createData();
            setVisibility(VISIBLE);
        }
    }

    private void createData() {
        mListAdd.clear();
        if (isCanLoop) {
            currentPosition = 1;
            for (int i = 0; i < mList.size() + 2; i++) {
                if (i == 0) {   //  判断当i=0为该处的mList的最后一个数据作为mListAdd的第一个数据
                    mListAdd.add(mList.get(mList.size() - 1));
                } else if (i == mList.size() + 1) {   //  判断当i=mList.size()+1时将mList的第一个数据作为mListAdd的最后一个数据
                    mListAdd.add(mList.get(0));
                } else {  //  其他情况
                    mListAdd.add(mList.get(i - 1));
                }
            }
        } else {
            mListAdd.addAll(mList);
        }
    }

    //  设置触摸事件，当滑动或者触摸时停止自动轮播
    private void setTouchListener() {
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isLooping = true;
                        stopLoop();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isLooping = false;
                        startLoop();
                    default:
                        break;
                }
                return false;
            }
        });
    }


    public void startLoop() {
        if (!isLooping && isAutoPlay && mViewPager != null) {
            mHandler.postDelayed(mRunnable, interval);// 每interval秒执行一次runnable.
            isLooping = true;
        }
    }

    public void stopLoop() {
        if (isLooping && mViewPager != null) {
            mHandler.removeCallbacks(mRunnable);
            isLooping = false;
        }
    }

    //  设置轮播小圆点
    private void setIndicator() {
        mDotList.clear();
        mLlIndicator.removeAllViews();
        //  设置LinearLayout的子控件的宽高，这里单位是像素。
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) indicatorRadius * 2, (int) indicatorRadius * 2);
        params.rightMargin = (int) (indicatorRadius * 2 / 1.5);
        if (mList.size() > 1) {
            //  for循环创建mUrlList.size()个ImageView（小圆点）
            for (int i = 0; i < mList.size(); i++) {
                DotView dotView = new DotView(getContext());
                dotView.setLayoutParams(params);
                dotView.setNormalColor(indicatorNormalColor);
                dotView.setCheckedColor(indicatorCheckedColor);
                dotView.setChecked(false);

                mLlIndicator.addView(dotView);
                mDotList.add(dotView);
            }
        }
        //设置第一个小圆点图片背景为红色
        if (mList.size() > 1) {
            mDotList.get(dotPosition).setChecked(true);
        }
        setIndicatorGravity();
    }

    private void setViewPager() {
        adapter = new MultiPagerAdapter<>(mListAdd, this, holderCreator);
        adapter.setCanLoop(isCanLoop);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition);

        setViewPagerScroller();

        setPageChangeListener();
        startLoop();
        setTouchListener();
        if (showIndicator) {
            mLlIndicator.setVisibility(VISIBLE);
        } else {
            mLlIndicator.setVisibility(GONE);
        }
    }

    public void setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        if (mViewPager == null) {
            return;
        }
        if (pageTransformer == null) {
            return;
        }

        mViewPager.setPageTransformer(true, pageTransformer);
    }

    private void setViewPagerScroller() {
        ViewPagerScroller scroller = new ViewPagerScroller(mViewPager.getContext());
        scroller.setScrollDuration(mScrollDuration);
        scroller.initViewPagerScroll(mViewPager);//这个是设置切换过渡时间
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    //  是否显示轮播指示器
    public void isShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
    }

    public void setIndicatorGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setPages(List<T> list, HolderCreator<ViewHolder> holderCreator) {
        if (list == null || holderCreator == null) {
            return;
        }
        mList.addAll(list);
        this.holderCreator = holderCreator;
    }

    //  ViewPager页面改变监听
    private void setPageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //  当state为SCROLL_STATE_IDLE即没有滑动的状态时切换页面
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager.setCurrentItem(currentPosition, false);
                }
            }
        });
    }

    private void pageSelected(int position) {
        if (isCanLoop) {
            if (position == 0) {    //判断当切换到第0个页面时把currentPosition设置为list.size(),即倒数第二个位置，小圆点位置为length-1
                currentPosition = mList.size();
                dotPosition = mList.size() - 1;
            } else if (position == mList.size() + 1) {    //当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
                currentPosition = 1;
                dotPosition = 0;
            } else {
                currentPosition = position;
                dotPosition = position - 1;
            }
            //  把之前的小圆点设置背景为暗红，当前小圆点设置为红色
            mDotList.get(prePosition).setChecked(false);
            mDotList.get(dotPosition).setChecked(true);
            prePosition = dotPosition;
        } else {
            currentPosition = position;
            //  把之前的小圆点设置背景为暗红，当前小圆点设置为红色
            mDotList.get(prePosition).setChecked(false);
            mDotList.get(currentPosition).setChecked(true);
            prePosition = currentPosition;
        }

    }

    public interface OnPageClickListener {
        void onPageClick(int position);
    }

    //  adapter中图片点击的回掉方法
    public void imageClick(int position) {
        if (isCanLoop) {
            if (mOnPageClickListener != null)
                mOnPageClickListener.onPageClick(position - 1);
        } else {
            if (mOnPageClickListener != null)
                mOnPageClickListener.onPageClick(position);
        }
    }

    /**
     * @param checkedColor 选中时指示器颜色
     * @param normalColor  未选中时指示器颜色
     */
    public void setIndicatorColor(@ColorInt int normalColor, @ColorInt int checkedColor) {
        indicatorCheckedColor = checkedColor;
        indicatorNormalColor = normalColor;
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.mOnPageClickListener = onPageClickListener;
    }

    public boolean isAutoPlay() {
        return isAutoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

    public boolean isCanLoop() {
        return isCanLoop;
    }

    public void setCanLoop(boolean canLoop) {
        isCanLoop = canLoop;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public List<T> getList() {
        return mListAdd;
    }

    public void setList(List<T> list) {
        mList = list;
        mListAdd = new ArrayList<>();
    }

    public float getIndicatorRadius() {
        return indicatorRadius;
    }

    public void setIndicatorRadius(float indicatorRadius) {
        this.indicatorRadius = DisplayUtils.dp2px(getContext(), indicatorRadius);
    }

    /**
     * 重新加载
     *
     * @param list 数据源
     */
    public void onReload(List<T> list) {
        if (list.isEmpty() || list == null) {
            return;
        }
        stopLoop();

        updateData(list);
        setIndicator();
        adapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(1, true);
        startLoop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        startLoop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        stopLoop();
    }

}
