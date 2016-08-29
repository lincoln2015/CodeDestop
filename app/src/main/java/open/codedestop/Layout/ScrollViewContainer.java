package open.codedestop.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import open.codedestop.Activity.LessonActivity;
import open.codedestop.Util.LessonAction;

/**
 ** Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/29.
 */
public class ScrollViewContainer extends LinearLayout {
    private String TAG = "ScrollViewContainer";

    // for scroll back main activity start
    private  int x = 0;
    private  int y = 0;

    private int downX = 0;
    private int downY = 0;

    private int lastX = downX;
    private int lastY = downY;

    private int lastUpPostion = 0;
    private int currentPage = 0;

    private boolean isLongPress = false;

    private int count ;
    private int width ;
    private int height ;
    LessonAction mAction;
    // for scroll back main activity end

    public void setLessonAction(LessonAction action){
        mAction = action;
    }

    public ScrollViewContainer(Context context) {
        super(context);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //  return super.onInterceptTouchEvent(ev);
        Log.i(TAG, "onInterceptTouchEvent");

        int x = (int)ev.getX();
        int y = (int)ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            downX = x;
            downY = y;
            lastX = downX;
            lastY = downY;
        }
        return false;
    }

    public int  getDownX(){
        return downX;
    }

    public int  getDownY(){
        return lastY;
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       // return super.onTouchEvent(event);
        Log.i(TAG, "onTouchEvent");
        int x = (int)event.getX();
        int y = (int)event.getY();
        int width = getMeasuredWidth();
        //  Log.i(TAG, "onTouch, X:"+x +",Y:"+y +",width:"+width);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                lastX = downX;
                lastY = downY;
                Log.i(TAG, "onTouch down  downX:" + downX + ",downY:" + downY);
                break;
            case MotionEvent.ACTION_MOVE:
                lastX = x;
                lastY = y;
                Log.i(TAG, "onTouch move  lastX:"+lastX +",lastY:"+lastY);
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouch up  lastX:"+lastX +",downX:"+downX+",abs x:"+Math.abs(lastX - downX));
                if (Math.abs(lastX - downX) > width / 2){
                  ///  finish();
                    mAction.onExitLesson();
                }
                break;
            default:
                break;
        }
        return false;
    }*/
}
