package com.asen.callphone.base.view;

/**
 * Created by asus on 2018/2/10.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.asen.callphone.R;


/**
 * 【Item侧滑删除菜单】
 * 继承自 ViewGroup ，实现滑动出现删除等选项的效果，
 * 思路：跟随手势将 item 向左滑动，
 * 在 onMeasure 时 将第一个 Item 设为屏幕宽度
 * 【解决屏幕上多个侧滑删除菜单】：内设一个类静态View类型变量 ViewCache，存储的是当前正处于右滑状态的 CstSwipeMenuItemViewGroup，
 * 每次 Touch 时对比，如果两次 Touch 的不是一个 View ，那么令 ViewCache 恢复普通状态，并且设置新的 CacheView
 * 只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
 * <p/>
 * 平滑滚动使用的是 Scroller ,20160811，最新平滑滚动又用属性动画做了，因为这样更酷炫(设置加速器不同)
 * <p/>
 * 20160824,fix 【多指一起滑我的情况】：只接第一个客人(使用一个类静态布尔变量)
 * other:
 * 1 菜单处于侧滑时，拦截长按事件
 * 2 解决侧滑时 点击 的冲突
 * 3 通过 isIos 变量控制是否是 IOS 阻塞式交互，默认是打开的。
 * 4 通过 isSwipeEnable 变量控制是否开启右滑菜单，默认打开。（某些场景，复用item，没有编辑权限的用户不能右滑）
 * 5 2016 09 29 add,，通过开关 isLeftSwipe支持左滑右滑
 * 6 2016 10 21 add , 增加viewChache 的 get()方法，可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。
 * 7 2016 10 22 fix , 当父控件宽度不是全屏时的bug。
 * 2016 10 22 add , 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的区域，关闭侧滑菜单。
 * 8 2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
 * 9 2016 11 04 fix 长按事件和侧滑的冲突。
 * 10 2016 11 09 add,适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度。
 * 11 2016 11 14 add,支持padding,且后续计划加入上滑下滑，因此不再支持ContentItem的margin属性。
 * 2016 11 14 add,修改回弹的动画，更平滑。
 * 2016 11 14 fix,微小位移的move不回回弹的bug
 * 2016 11 18,fix 当ItemView存在高度可变的情况
 * 2016 12 07,fix 禁止侧滑时(isSwipeEnable false)，点击事件不受干扰。
 * 2016 12 09,fix ListView快速滑动快速删除时，偶现菜单不消失的bug。
 * Created by zhangxutong .
 * Date: 16/04/24
 */
public class SwipeMenuLayout extends ViewGroup {
    //region
    //endregion

    //region 设置的属性


    private static final String TAG = "zxt/SwipeMenuLayout";

    /**
     * 为了处理单击事件的冲突
     */
    private int mScaleTouchSlop;

    /**
     * 计算滑动速度用
     */
    private int mMaxVelocity;

    /**
     * 多点触摸只算第一根手指的速度
     */
    private int mPointerId;

    /**
     * 自己的高度
     */
    private int mHeight;

    /**
     * 右侧菜单宽度总和(最大滑动距离)
     */
    private int mRightMenuWidths;

    /**
     * 滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu
     */
    private int mLimit;

    /**
     * 2016 11 13 add ，存储contentView(第一个View)
     */
    private View mContentView;

    /**
     * 以前item的滑动动画靠它做，现在用属性动画做
     */
    //private Scroller mScroller;

    /**
     * 上一次的 xy 坐标
     */
    private PointF mLastP = new PointF();

    /**
     * 2016 10 22 add , 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的区域，关闭侧滑菜单。
     * 增加一个布尔值变量，dispatch 函数里，每次 down 时，为true，move 时判断，如果是滑动动作，设为false。
     * 在 Intercept 函数的up时，判断这个变量，如果仍为true 说明是点击事件，则关闭菜单。
     */
    private boolean isUnMoved = true;

    /**
     * 2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
     * up-down的坐标，判断是否是滑动，如果是，则屏蔽一切点击事件
     */
    private PointF mFirstP = new PointF();
    private boolean isUserSwiped;

    /**
     * 存储的是当前正在展开的View
     */
    private static SwipeMenuLayout mViewCache;

    /**
     * 防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空
     */
    private static boolean isTouching;

    /**
     * 滑动速度变量（计算触摸移动或限制最大速度）
     */
    private VelocityTracker mVelocityTracker;
    private android.util.Log LogUtils;

    /**
     * 右滑删除功能的开关,默认开（true:滑动；false：不滑动）
     */
    private boolean isSwipeEnable;

    /**
     * IOS、QQ式交互，默认开
     */
    private boolean isIos;

    /**
     * IOS类型下，是否拦截事件的flag
     */
    private boolean iosInterceptFlag;

    /**
     * 20160929add 左滑右滑的开关,默认左滑打开菜单(true:从右向左滑动；false：从左向右滑动)
     */
    private boolean isLeftSwipe;

    /**
     * 平滑展开（插值器，也叫加速器）
     */
    private ValueAnimator mExpandAnim, mCloseAnim;

    /**
     * 代表当前是否是展开状态 2016 11 03 add
     */
    private boolean isExpand;


    //endregion


    //region 设置、获取活动的判断方法


    /**
     * 获取右滑删除功能的开关
     */
    public boolean isSwipeEnable() {
        return isSwipeEnable;
    }

    /**
     * 设置侧滑功能开关
     *
     * @param swipeEnable
     */
    public void setSwipeEnable(boolean swipeEnable) {
        isSwipeEnable = swipeEnable;
    }

    /**
     * 获取IOS阻塞式交互
     */
    public boolean isIos() {
        return isIos;
    }

    /**
     * 设置是否开启IOS阻塞式交互
     *
     * @param ios
     */
    public SwipeMenuLayout setIos(boolean ios) {
        isIos = ios;
        return this;
    }

    /**
     * 获取是否开启左滑出菜单，返回 false 为右滑出菜单
     */
    public boolean isLeftSwipe() {
        return isLeftSwipe;
    }

    /**
     * 设置是否开启左滑出菜单，设置 false 为右滑出菜单
     *
     * @param leftSwipe
     * @return
     */
    public SwipeMenuLayout setLeftSwipe(boolean leftSwipe) {
        isLeftSwipe = leftSwipe;
        return this;
    }

    /**
     * 返回ViewCache
     *
     * @return
     */
    public static SwipeMenuLayout getViewCache() {
        return mViewCache;
    }

    //endregion


    //region 构造函数

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop(); //是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();//获得允许执行一个fling手势动作的最大速度值
        //初始化滑动帮助类对象
        //mScroller = new Scroller(context);

        //右滑删除功能的开关,默认开
        isSwipeEnable = true; // true:滑动；false：不滑动
        //IOS、QQ式交互，默认开
        isIos = true;
        //左滑右滑的开关,默认左滑打开菜单
        isLeftSwipe = true;// true:从右向左滑动；false：从左向右滑动
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0);
        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            //如果引用成AndroidLib 资源都不是常量，无法使用switch case
            if (attr == R.styleable.SwipeMenuLayout_swipeEnable) {
                isSwipeEnable = ta.getBoolean(attr, true);
            } else if (attr == R.styleable.SwipeMenuLayout_ios) {
                isIos = ta.getBoolean(attr, true);
            } else if (attr == R.styleable.SwipeMenuLayout_leftSwipe) {
                isLeftSwipe = ta.getBoolean(attr, true);
            }
        }
        ta.recycle(); // 获取完反射属性后，一定要记得调用次方法回收

    }


    /**
     * 这个方法主要是用于父容器添加子View时调用。(添加多少个View就执行多少次)
     * 用于生成和此容器类型相匹配的布局参数类。
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    //endregion


    //region android View/ViewGroup的生命周期-自定义

    /**
     * android View/ViewGroup的生命周期-自定义
     * view:onAttachToWindow —>onMeasure —>onSizeChanged —>onLayout —>(onDraw)dispatchDraw —>
     * onMeasure —>onLayout —>(onDraw)dispatchDraw —>onDetachedFromWindow(脱离窗户)
     * <p>
     * 1.首先，Activity onCreate并初始化view
     * 2.然后，Activity onResume后调用view的onAttachedToWindow，因此常常在onAttachedToWindow方法中做初始化工作，比如注册一些广播、开始动画等等……
     * 3.接下来，如果设置了背景则调用onDraw，之后倘若是viewgroup则调用dispatchDraw。
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * android View/ViewGroup的生命周期-自定义
     * view:onAttachToWindow —>onMeasure —>onSizeChanged —>onLayout —>(onDraw)dispatchDraw —>
     * onMeasure —>onLayout —>(onDraw)dispatchDraw —>onDetachedFromWindow(脱离窗户)
     * <p>
     * 理论上我们可以很好的理解onMeasure()，onLayout()，onDraw()这三个函数：
     * 1.View本身大小多少，这由onMeasure()决定；
     * 2.View在ViewGroup中的位置如何，这由onLayout()决定；
     * 3.绘制View，onDraw()定义了如何绘制这个View。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setClickable(true);   //令自己可点击，从而获取触摸事件

        mRightMenuWidths = 0; //由于 ViewHolder 的复用机制，每次这里要手动恢复初始值
        mHeight = 0;
        int contentWidth = 0; //2016 11 09 add,适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度
        int childCount = getChildCount(); // 获取在次自定义容器中的所有子控件

        //add by 2016 08 11 为了子View的高，可以matchParent(参考的FrameLayout 和LinearLayout的Horizontal);当容器的高为MatchParent的时候measureMatchParentChildren=true；
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY; // 测量规格MeasureSpec ； MeasureSpec.EXACTLY：精确地
        boolean isNeedMeasureChildHeight = false;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //令每一个子View可点击，从而获取触摸事件
            childView.setClickable(true);
            if (childView.getVisibility() != GONE) {
                //后续计划加入上滑、下滑，则将不再支持Item的margin
                measureChild(childView, widthMeasureSpec, heightMeasureSpec); //设置ViewGroup的某一个子view，多宽，多高, 内部加上了viewGroup的padding值
                //measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0); //设置 ViewGroup 的某一个子view，多宽，多高, 内部加上了viewGroup的padding值、margin值和传入的宽高wUsed、hUsed
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight()/* + lp.topMargin + lp.bottomMargin*/);
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true;
                }
                if (i > 0) //第一个布局是Left item，从第二个开始才是RightMenu
                {
                    mRightMenuWidths += childView.getMeasuredWidth();
                } else {
                    mContentView = childView;
                    contentWidth = childView.getMeasuredWidth();
                }
            }
        }
        // 设置 ViewGroup 的测量尺寸(宽高)
        setMeasuredDimension(getPaddingLeft() + getPaddingRight() + contentWidth, mHeight + getPaddingTop() + getPaddingBottom());
        //宽度取第一个Item(Content)的宽度
        mLimit = mRightMenuWidths * 4 / 10;//滑动判断的临界值
        //Log.d(TAG, "onMeasure() called with: " + "mRightMenuWidths = [" + mRightMenuWidths);
        if (isNeedMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }


    /**
     * 给MatchParent的子View设置高度
     *
     * @param count
     * @param widthMeasureSpec
     * @see android.widget.LinearLayout# 同名方法
     */
    private void forceUniformHeight(int count, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(
                getMeasuredHeight(),
                MeasureSpec.EXACTLY
        );//以父布局高度构建一个 Exactly（准确） 的测量参数
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    int oldWidth = lp.width;//measureChildWithMargins 这个函数会用到宽，所以要保存一下
                    lp.width = child.getMeasuredWidth(); // 對 View 上的內容進行測量後得到的View內容佔據的寬度
                    // Remeasure with new dimensions //设置 ViewGroup 的某一个子view，多宽，多高, 内部加上了viewGroup的padding值、margin值和传入的宽高wUsed、hUsed
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }


    /**
     * android View/ViewGroup的生命周期-自定义
     * view:onAttachToWindow —>onMeasure —>onSizeChanged —>onLayout —>(onDraw)dispatchDraw —>
     * onMeasure —>onLayout —>(onDraw)dispatchDraw —>onDetachedFromWindow(脱离窗户)
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * android View/ViewGroup的生命周期-自定义
     * view:onAttachToWindow —>onMeasure —>onSizeChanged —>onLayout —>(onDraw)dispatchDraw —>
     * onMeasure —>onLayout —>(onDraw)dispatchDraw —>onDetachedFromWindow(脱离窗户)
     * <p>
     * 理论上我们可以很好的理解onMeasure()，onLayout()，onDraw()这三个函数：
     * 1.View本身大小多少，这由onMeasure()决定；
     * 2.View在ViewGroup中的位置如何，这由onLayout()决定；
     * 3.绘制View，onDraw()定义了如何绘制这个View。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //LogUtils.e(TAG, "onLayout() called with: " + "changed = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
        int childCount = getChildCount();
        int left = 0 + getPaddingLeft(); // 获取 ViewGroup 的 左 Padd
        int right = 0 + getPaddingLeft(); // 获取 ViewGroup 的 右 Padd
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0) //第一个子View是内容 宽度设置为全屏
                {
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                    left = left + childView.getMeasuredWidth();
                } else {
                    if (isLeftSwipe) {
                        childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                        left = left + childView.getMeasuredWidth();
                    } else {
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right, getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth(); // childView.getMeasuredWidth(): 對View上的內容進行測量後得到的View內容佔據的寬度
                    }

                }
            }
        }
        //Log.d(TAG, "onLayout() called with: " + "maxScrollGap = [" + maxScrollGap + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
    }

    /**
     * android View/ViewGroup的生命周期-自定义
     * view:onAttachToWindow —>onMeasure —>onSizeChanged —>onLayout —>(onDraw)dispatchDraw —>
     * onMeasure —>onLayout —>(onDraw)dispatchDraw —>onDetachedFromWindow(脱离窗户)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * android View/ViewGroup的生命周期-自定义
     * view:onAttachToWindow —>onMeasure —>onSizeChanged —>onLayout —>(onDraw)dispatchDraw —>
     * onMeasure —>onLayout —>(onDraw)dispatchDraw —>onDetachedFromWindow(脱离窗户)
     * <p>
     * 如果设置了背景则调用onDraw，之后倘若是viewgroup则调用dispatchDraw。
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    /**
     * android View/ViewGroup的生命周期-自定义
     * view:onAttachToWindow —>onMeasure —>onSizeChanged —>onLayout —>(onDraw)dispatchDraw —>
     * onMeasure —>onLayout —>(onDraw)dispatchDraw —>onDetachedFromWindow(脱离窗户)
     * Activity onDestroy后才调用onDetachedFromWindow，这时我们就在这个方法做一些收尾工作，如：取消广播注册、停止动画等等。加载多少个View，activity退出的时候就调用多少次
     * <p>
     * 每次ViewDetach的时候，判断一下 ViewCache是不是自己，如果是自己，关闭侧滑菜单，且ViewCache设置为null，
     * 理由：
     * 1 防止内存泄漏(ViewCache是一个静态变量)
     * 2 侧滑删除后自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
     */
    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.smoothClose();
            mViewCache = null;
        }
        super.onDetachedFromWindow();
    }

    //endregion


    //region 触摸事件

    /**
     * 常说事件传递中的流程是:dispatchTouchEvent->onInterceptTouchEvent->onTouchEvent
     * dispatchTouchEvent是处理触摸事件分发,事件(多数情况)是从Activity的dispatchTouchEvent开始的。执行super.dispatchTouchEvent(ev)，事件向下分发。
     * onInterceptTouchEvent是ViewGroup提供的方法，默认返回false，返回true表示拦截。
     * onTouchEvent是View中提供的方法，ViewGroup也有这个方法，view中不提供onInterceptTouchEvent。view中默认返回true，表示消费了这个事件。
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // LogUtils.d(TAG, "dispatchTouchEvent() called with: " + "ev = [" + ev + "]");
        if (isSwipeEnable) {
            acquireVelocityTracker(ev); // 获得速度跟踪
            final VelocityTracker verTracker = mVelocityTracker;
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isUserSwiped = false;//2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    isUnMoved = true;//2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                    iosInterceptFlag = false;//add by 2016 09 11 ，每次DOWN时，默认是不拦截的
                    if (isTouching) //如果有别的指头摸过了，那么就return false。这样后续的move..等事件也不会再来找这个View了。
                    {
                        return false;
                    } else {
                        isTouching = true;//第一个摸的指头，赶紧改变标志，宣誓主权。
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    mFirstP.set(ev.getRawX(), ev.getRawY());//2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。

                    //如果down，view和cacheview不一样，则立马让它还原。且把它置为null
                    if (mViewCache != null) {
                        if (mViewCache != this) {
                            mViewCache.smoothClose();

                            iosInterceptFlag = isIos;//add by 2016 09 11 ，IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。
                        }
                        //只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
                        getParent().requestDisallowInterceptTouchEvent(true); // true:阻止父view拦截点击事件,剥夺父view 对touch 事件的处理权
                    }
                    //求第一个触点的id， 此时可能有多个触点，但至少一个，计算滑动速率用
                    mPointerId = ev.getPointerId(0);
                    break;

                case MotionEvent.ACTION_MOVE:
                    //add by 2016 09 11 ，IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (iosInterceptFlag) {
                        break;
                    }

                    float gap = mLastP.x - ev.getRawX(); // 缺口

                    //为了在水平滑动中禁止父类ListView等再竖直滑动
                    if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) //2016 09 29 修改此处，使屏蔽父布局滑动更加灵敏；Math.abs(gap)：返回一个double值的绝对值。如果参数为负，则返回该参数。
                    {
                        getParent().requestDisallowInterceptTouchEvent(true);// true:阻止父view拦截点击事件,剥夺父view 对touch 事件的处理权
                    }

                    //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。begin
                    if (Math.abs(gap) > mScaleTouchSlop) // 滑动距离大于点击的距离，说明现在不是点击事件，而是滑动事件
                    {
                        isUnMoved = false;
                    }

                    //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。end
                    //如果scroller还没有滑动结束 停止滑动动画
/*                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }*/

                    scrollBy((int) (gap), 0);//滑动使用scrollBy
                    //越界修正
                    if (isLeftSwipe) //左滑
                    {
                        if (getScrollX() < 0) {
                            scrollTo(0, 0);
                        }
                        if (getScrollX() > mRightMenuWidths) {
                            scrollTo(mRightMenuWidths, 0);
                        }
                    } else //右滑
                    {
                        if (getScrollX() < -mRightMenuWidths) {
                            scrollTo(-mRightMenuWidths, 0);
                        }
                        if (getScrollX() > 0) {
                            scrollTo(0, 0);
                        }
                    }

                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                        isUserSwiped = true;
                    }

                    //add by 2016 09 11 ，IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!iosInterceptFlag) //且滑动了 才判断是否要收起、展开menu
                    {
                        //求伪瞬时速度
                        verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                        final float velocityX = verTracker.getXVelocity(mPointerId);
                        if (Math.abs(velocityX) > 1000) //滑动速度超过阈值
                        {
                            if (velocityX < -1000) {
                                if (isLeftSwipe) //左滑
                                {
                                    smoothExpand(); //平滑展开Menu
                                } else {
                                    smoothClose(); //平滑关闭Menu
                                }
                            } else {
                                if (isLeftSwipe) //左滑
                                {
                                    smoothClose(); // 平滑关闭Menu
                                } else {
                                    smoothExpand(); //平滑展开Menu
                                }
                            }
                        } else {
                            if (Math.abs(getScrollX()) > mLimit) //否则就判断滑动距离
                            {
                                smoothExpand(); //平滑展开Menu
                            } else {
                                smoothClose(); // 平滑关闭Menu
                            }
                        }
                    }

                    releaseVelocityTracker(); //释放
                    //LogUtils.i(TAG, "onTouch A ACTION_UP ACTION_CANCEL:velocityY:" + velocityX);
                    isTouching = false;//没有手指在摸我了
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }


    /**
     * 释放VelocityTracker
     *
     * @see VelocityTracker#clear()
     * @see VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    /**
     * 常说事件传递中的流程是:dispatchTouchEvent->onInterceptTouchEvent->onTouchEvent
     * dispatchTouchEvent是处理触摸事件分发,事件(多数情况)是从Activity的dispatchTouchEvent开始的。执行super.dispatchTouchEvent(ev)，事件向下分发。
     * onInterceptTouchEvent是ViewGroup提供的方法，默认返回false，返回true表示拦截。
     * onTouchEvent是View中提供的方法，ViewGroup也有这个方法，view中不提供onInterceptTouchEvent。view中默认返回true，表示消费了这个事件。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //add by zhangxutong 2016 12 07 begin:
        //禁止侧滑时，点击事件不受干扰。
        if (isSwipeEnable) {
            int a = ev.getAction();
            switch (ev.getAction()) {
                //add by zhangxutong 2016 11 04 begin :
                // fix 长按事件和侧滑的冲突。
                case MotionEvent.ACTION_MOVE:
                    //屏蔽滑动时的事件
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                        return true;
                    }
                    break;
                //add by zhangxutong 2016 11 04 end
                case MotionEvent.ACTION_UP:
                    //为了在侧滑时，屏蔽子View的点击事件
                    if (isLeftSwipe) {
                        if (getScrollX() > mScaleTouchSlop) {
                            //add by 2016 09 10 解决一个智障问题~ 居然不给点击侧滑菜单 我跪着谢罪
                            //这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的的。。。
                            if (ev.getX() < getWidth() - getScrollX()) {
                                //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (isUnMoved) {
                                    smoothClose();
                                }
                                return true;//true表示拦截
                            }
                        }
                    } else {
                        if (-getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() > -getScrollX()) //点击范围在菜单外 屏蔽
                            {
                                //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (isUnMoved) {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    }
                    //add by zhangxutong 2016 11 03 begin:
                    // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (isUserSwiped) {
                        return true;
                    }
                    //add by zhangxutong 2016 11 03 end

                    break;
            }
            //模仿IOS 点击其他区域关闭：
            if (iosInterceptFlag) {
                //IOS模式开启，且当前有菜单的View，且不是自己的 拦截点击事件给子View
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 展开时，禁止长按（就是item 活动删除出来的后，禁止设置长按点击监听功能）
     */
    @Override
    public boolean performLongClick() {
        if (Math.abs(getScrollX()) > mScaleTouchSlop) {
            return false;
        }
        return super.performLongClick();
    }

    //endregion


    /**
     * 菜单展开判断
     */
    public void smoothExpand() {
        //Log.d(TAG, "smoothExpand() called" + this);
        /*mScroller.startScroll(getScrollX(), 0, mRightMenuWidths - getScrollX(), 0);
        invalidate();*/
        //展开就加入ViewCache：
        mViewCache = SwipeMenuLayout.this;

        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(false);
        }

        cancelAnim();
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), isLeftSwipe ? mRightMenuWidths : -mRightMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new OvershootInterpolator());
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = true;
            }
        });
        mExpandAnim.setDuration(300).start();
    }


    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private void cancelAnim() {
        if (mCloseAnim != null && mCloseAnim.isRunning()) {
            mCloseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }
    }


    /**
     * 平滑关闭
     */
    public void smoothClose() {
        //Log.d(TAG, "smoothClose() called" + this);
/*        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        invalidate();*/
        mViewCache = null;

        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(true);
        }

        cancelAnim();
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AccelerateInterpolator());
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = false;

            }
        });
        mCloseAnim.setDuration(300).start();
        //LogUtils.d(TAG, "smoothClose() called with:getScrollX() " + getScrollX());
    }


    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    public void quickClose() {
        if (this == mViewCache) {
            //先取消展开动画
            cancelAnim();
            mViewCache.scrollTo(0, 0);//关闭滚动
            mViewCache = null; // 设置改视图为null，相当于删除该视图
        }
    }


    //region //平滑滚动 弃用 改属性动画实现
/*
    @Override
    public void computeScroll() {
        //判断Scroller是否执行完毕：
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate();
        }
    }
    */
    //endregion


}

