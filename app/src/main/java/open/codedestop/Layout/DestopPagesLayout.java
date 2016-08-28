package open.codedestop.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * * Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/28.
 */
public class DestopPagesLayout extends ViewGroup {
    private final  String TAG = "DestopPagesLayout";

    private  int x = 0;
    private  int y = 0;

    private int mDownX = 0;
    private int mDownY = 0;

    private int mLastX = mDownX;
    private int mLastY = mDownX;

    private int mLastUpPostion = 0;
    private int mCurrentPage = 0;

    private boolean mIsLongPress = false;

    private int mCount ;
    private int mWidth ;
    private int mHeight ;

    private int mPageWidth;
    private int mPageHeight;

    private Scroller mScroller;
    boolean mIntercept = false;
    private VelocityTracker mVelocityTracker;

    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;

    public DestopPagesLayout(Context context) {
        super(context);
        mScroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public DestopPagesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public DestopPagesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public DestopPagesLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure");
        View view = (View)getParent();
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++){
            View child = getChildAt(i);
            Log.i(TAG, "onMeasure child["+i+"]:"+child.getMeasuredWidth()+","+child.getMeasuredHeight());
            Log.i(TAG, "onMeasure width/hegith child[" + i + "]:" + child.getWidth() + "," + child.getHeight());
            // child.measure(child.getMeasuredWidth(), child.getMeasuredHeight());
            // child.measure(child.getMeasuredWidth(), child.getMeasuredHeight());
            child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        }

        Log.i(TAG, "onMeasure parent:" + widthSize + "," + heightSize);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mCount = getChildCount();
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mPageWidth = mWidth;

        int left;
        int right ;
        int top ;
        int bottom ;
        Log.i(TAG, "onLayout");

        left = 0;
        for (int i = 0; i < mCount; i++){
            View child = getChildAt(i);
            MarginLayoutParams layoutPara = (MarginLayoutParams) child.getLayoutParams();
            Log.i(TAG, "onLayout: width:"+child.getMeasuredWidth() +",height:"+child.getMeasuredHeight());
            left = left + layoutPara.leftMargin;
            right = left + child.getMeasuredWidth() - layoutPara.rightMargin;
            top = 0 + layoutPara.topMargin;
            bottom = top + child.getMeasuredHeight() - layoutPara.bottomMargin;
            child.layout(left, top, right, bottom);
            left = left - layoutPara.leftMargin;
            left = right + layoutPara.rightMargin;
        }
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                if (xDiff > mTouchSlop) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                        : TOUCH_STATE_SCROLLING;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }

        return mTouchState != TOUCH_STATE_REST;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        int width = getMeasuredWidth();
        int count = getChildCount();

        if (count > 0)
            width = getChildAt(0).getMeasuredWidth();

        Log.i(TAG, "onTouchEvent, X:"+x +",Y:"+y );

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                mLastX = mDownX;
                mLastY = mDownY;

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:

                int deltaX = (int) (mLastMotionX - x);
                scrollBy(deltaX, 0);

                mLastMotionX = x;
                mLastX = x;
                mLastY = y;


                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mLastX - mDownX) < width / 2){
                }
                else if (mLastX > mDownX){
                    if (mCurrentPage > 0) {
                        mCurrentPage--;
                    }
                }else if (mLastX < mDownX){
                    if (mCurrentPage < count - 1){
                        mCurrentPage++;
                    }
                }
                // scrollTo(currentPage * width, 0);
                int delta = mCurrentPage * getWidth() - getScrollX();
                mScroller.startScroll(getScrollX(), 0, delta, 0,
                        Math.abs(delta) * 2);

                invalidate(); // Redraw the layout
                onScreenChangeListener.onScreenChange(mCurrentPage);
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
            default:
                break;
        }
        return true;
    }

    public void setToPage(int page){
        scrollTo(page * mPageWidth, 0);
        mCurrentPage = page;
    }

    public int getCurrentPage()
    {
        return mCurrentPage;
    }

    //page the pageindicator show & update
    public interface OnScreenChangeListener {
        void onScreenChange(int newpage);
    }

    private OnScreenChangeListener onScreenChangeListener;

    public void setOnScreenChangeListener(
            OnScreenChangeListener onScreenChangeListener) {
        this.onScreenChangeListener = onScreenChangeListener;
    }


}