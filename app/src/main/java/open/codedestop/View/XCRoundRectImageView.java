package open.codedestop.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;

/**
 ** Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/28.
 * user for the rotatable imageview with text
 */
public class XCRoundRectImageView extends TwoStateImageView implements Rotatable {

    private static final String TAG = "XCRoundRectImageView";
    private static final int ANIMATION_SPEED = 400; // 270 deg/sec
    private int mCurrentDegree = 0; // [0, 359]
    private int mStartDegree = 0;
    private int mTargetDegree = 0;
    private boolean mClockwise = false;
    private boolean mEnableAnimation = true;
    private long mAnimationStartTime = 0;
    private long mAnimationEndTime = 0;
    // add from XCRoundRectImageView
    private Paint paint;
    // add end
    int mRoundPx = 50;

    private String paintTextTitle =null;


    public void setDrawTextTitle(String txt)
    {
        paintTextTitle = txt;
    }

    public XCRoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint  = new Paint();
    }

    public XCRoundRectImageView(Context context, int roundPx) {
        this(context, null);
        mRoundPx = roundPx;
    }

    public XCRoundRectImageView(Context context) {
        this(context, null);
    }

    public XCRoundRectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        paint  = new Paint();
    }

    // Rotate the view counter-clockwise
    @Override
    public void setOrientation(int degree, boolean animation) {
        Log.d(TAG, "setOrientation(" + degree + ", " + animation + ") mOrientation="
                + mTargetDegree);
        mEnableAnimation = animation;
        // make sure in the range of [0, 359]
        degree = degree >= 0 ? degree % 360 : degree % 360 + 360;
        if (degree == mTargetDegree) {
            return;
        }
        mTargetDegree = degree;
        if (mEnableAnimation) {
            mStartDegree = mCurrentDegree;
            mAnimationStartTime = AnimationUtils.currentAnimationTimeMillis();
            int diff = mTargetDegree - mCurrentDegree;
            diff = diff >= 0 ? diff : 360 + diff; // make it in range [0, 359]
            // Make it in range [-179, 180]. That's the shorted distance between the two angles
            diff = diff > 180 ? diff - 360 : diff;
            mClockwise = diff >= 0;
            mAnimationEndTime = mAnimationStartTime + Math.abs(diff) * 1000 / ANIMATION_SPEED;
        } else {
            mCurrentDegree = mTargetDegree;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Bitmap b;
        Drawable drawable = getDrawable();
        if (drawable == null) {
            Log.e(TAG, "drawable == null, return");
            return;
        }

        Rect bounds = drawable.getBounds();
        int w = bounds.right - bounds.left;
        int h = bounds.bottom - bounds.top;

        if (w == 0 || h == 0) {
            Log.e(TAG, "w == 0 || h == 0, return");
            return; // nothing to draw
        }
        Log.e(TAG, "w:"+w +",h:"+h);

        if (mCurrentDegree != mTargetDegree) {
            long time = AnimationUtils.currentAnimationTimeMillis();
            if (time < mAnimationEndTime) {
                int deltaTime = (int) (time - mAnimationStartTime);
                int degree = mStartDegree + ANIMATION_SPEED * (mClockwise ? deltaTime : -deltaTime) / 1000;
                degree = degree >= 0 ? degree % 360 : degree % 360 + 360;
                mCurrentDegree = degree;
                invalidate();
            } else {
                mCurrentDegree = mTargetDegree;
            }
        }
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        int width = getWidth() - left - right;
        int height = getHeight() - top - bottom;

        // Log.e(TAG, "padding left:"+left +",right:"+right +",getwidht:"+getWidth() +",getheigh:"+getHeight() +",widht:"+width +",height:"+height);

        int saveCount = canvas.getSaveCount();

        // Scale down the image first if required.
        if ((getScaleType() == ImageView.ScaleType.FIT_CENTER) && ((width < w) || (height < h))) {
            float ratio = Math.min((float) width / w, (float) height / h);
            canvas.scale(ratio, ratio, width / 2.0f, height / 2.0f);
        }
        canvas.translate(left + width / 2, top + height / 2);
        canvas.rotate(-mCurrentDegree);
        canvas.translate(-w / 2, -h / 2);

        // add start
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        if (mRoundPx == 0){
            b = bitmap;
        }
        else {
            b = getRoundBitmap(bitmap, mRoundPx);
        }
        //Bitmap b = bitmap;
        final Rect rectSrc = new Rect(0, 0, w, h);
        final Rect rectDest = new Rect(0,0,w,h);
        paint.reset();
        canvas.drawBitmap(b, rectSrc, rectDest, paint);

        int textSize = 50;
        if (paintTextTitle != null)
        {
            paint.setColor(Color.WHITE);
            paint.setTextSize((int) (textSize / 1.3));
            if (paintTextTitle.length() > 10 && paintTextTitle.length() <= 13)
                canvas.drawText(paintTextTitle, rectDest.centerX() - width / 3 , rectDest.bottom + height /5, paint);
            else if (paintTextTitle.length() == 6)
                canvas.drawText(paintTextTitle, rectDest.centerX() - width / 4 , rectDest.bottom + height /5, paint);
            else if (paintTextTitle.length() == 8 || paintTextTitle.length() == 7 || paintTextTitle.length() == 9 || paintTextTitle.length() == 10)
                canvas.drawText(paintTextTitle, rectDest.centerX() - width / 4 , rectDest.bottom + height /5, paint);
            else if (paintTextTitle.length() > 13)
                canvas.drawText(paintTextTitle, rectDest.centerX() - width /3 - width /8 , rectDest.bottom +height /5, paint);
            else if (paintTextTitle.length() <= 3)
                canvas.drawText(paintTextTitle, rectDest.centerX() - width / 9, rectDest.bottom + height /5, paint);
            else
                canvas.drawText(paintTextTitle, rectDest.centerX() - width / 5, rectDest.bottom + height /5, paint);
        }

        canvas.restoreToCount(saveCount);
        // super.onDraw(canvas);
    }

    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}