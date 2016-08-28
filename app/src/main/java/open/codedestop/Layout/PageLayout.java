package open.codedestop.Layout;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * * Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/28.
 */
public class PageLayout extends ViewGroup {

    private final String TAG = "PageLayout";
    private final int mChildViewDefaultWidth = 300;
    private int mParentWidth;
    private int mParentHeight;
    private int mChildViewWidth = mChildViewDefaultWidth;
    private int mChildCount;
    private int mCountsOneRow = 3;
    private int mRealChildWidth ;
    private int mDefaultMargin = 20;


    public PageLayout(Context context) {
        super(context);
    }

    public PageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mChildCount = getChildCount();
        mParentWidth = getMeasuredWidth();
        mParentHeight = getMeasuredHeight();

        mChildViewWidth = Math.min(mParentWidth, mParentHeight) / mCountsOneRow;
        int childWidthSpec = MeasureSpec.makeMeasureSpec(mChildViewWidth, MeasureSpec.AT_MOST);
        int childHeightSpec = MeasureSpec.makeMeasureSpec(mChildViewWidth, MeasureSpec.AT_MOST);

        for (int i = 0; i < mChildCount; i++){
            View view = getChildAt(i);
            view.measure(childWidthSpec, childHeightSpec);
        }
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mChildCount = getChildCount();
        mParentWidth = getWidth();
        // childViewWidth = parentWidth / countsOneRow;
        mChildViewWidth = Math.min(mParentWidth, mParentHeight) / mCountsOneRow;
        int marginSpace = 0;

        for (int i = 0; i < mCountsOneRow; i++){
            View view = getChildAt(i);
            if (view != null){
                MarginLayoutParams layoutPara = (MarginLayoutParams) view.getLayoutParams();
                if (layoutPara != null){
                    marginSpace += layoutPara.leftMargin;
                    marginSpace += layoutPara.rightMargin;
                }
            }
        }
        int leftSpace = Math.min(mParentWidth, mParentHeight) - marginSpace;

        mRealChildWidth = leftSpace /  mCountsOneRow;

        int left = 0;
        int right = left;
        int top = 0;
        int bottom = top;

        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();

        int leftMargin;
        int rightMargin;
        int topMargin;
        int bottomMargin;

        if (getChildCount() > 0){
            leftMargin = ((MarginLayoutParams)getChildAt(0).getLayoutParams()).leftMargin;
            rightMargin = ((MarginLayoutParams)getChildAt(0).getLayoutParams()).rightMargin;
            topMargin = ((MarginLayoutParams)getChildAt(0).getLayoutParams()).topMargin;
            bottomMargin = ((MarginLayoutParams)getChildAt(0).getLayoutParams()).bottomMargin;
        }else{
            leftMargin = mDefaultMargin;
            rightMargin = mDefaultMargin;
            topMargin = mDefaultMargin;
            bottomMargin = mDefaultMargin;
        }

        for (int i = 0; i < mChildCount; i++){
            View view = getChildAt(i);
            if (i % mCountsOneRow == 0){
                left = leftPadding + leftMargin;
                right = left + mRealChildWidth;
                if (i == 0){
                    top  = topPadding + topMargin;
                    bottom = top + mRealChildWidth;
                }else{
                    top  += mRealChildWidth + topMargin;
                    bottom = top + mRealChildWidth;
                }
            }else{
                left += mRealChildWidth + rightMargin;
                right = left + mRealChildWidth;
            }
            Log.i(TAG, "padding_left:" + view.getPaddingLeft() + ",padding_right:" + view.getPaddingRight()
                    + ",paddding_top:" + view.getPaddingTop() + ",paddding_bottom:" + view.getPaddingBottom());
            view.layout(left, top, right, bottom);
        }
    }
}