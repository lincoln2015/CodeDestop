package open.codedestop.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import open.codedestop.Layout.DestopPagesLayout;
import open.codedestop.R;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import open.codedestop.Layout.DestopPagesLayout;
import open.codedestop.Layout.PageIndictorLayout;
import open.codedestop.Layout.PageLayout;
import open.codedestop.R;
import open.codedestop.View.XCRoundRectImageView;

/**
 ** Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/28.
 */
public class DestopActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "DestopActivity";
    private PageIndictorLayout mPageControl;
    private static final int MODE_DEFAULT_PADDING = 5;

    public static final int LESSON_TABLE_LAYOUT = 0;
    public static final int LESSON_PAGES_SCROLLLAYOUT = 1;
    public static final int LESSON_ANIMATION = 2;
    public static final int LESSON_VIDEOPLAYER = 3;
    public static final int LESSON_ROTATABLE = 4;
    public static final int LESSON_SEEKBAR = 5;
    public static final int LESSON_GESTURE_DETECTOR = 6;
    public static final int LESSON_PREFERENCE = 7;
    public static final int LESSON_BROADCAST_SERVICE = 8;
    public static final int LESSON_NET = 9;
    public static final int LESSON_10 = 10;
    public static final int LESSON_11 = 11;


    public static final int LESSON_NUM_ALL = 10;
    public static final int NUM_ONE_SCREEN = 9;
    private int mScreenNums;

    private static final int[] LESSON_ICONS_HIGHTLIGHT = new int[LESSON_NUM_ALL];
    static {
        LESSON_ICONS_HIGHTLIGHT[LESSON_TABLE_LAYOUT] = R.drawable.tablelayout;
        LESSON_ICONS_HIGHTLIGHT[LESSON_PAGES_SCROLLLAYOUT] = R.drawable.scrollpages;
        LESSON_ICONS_HIGHTLIGHT[LESSON_ANIMATION] = R.drawable.animation;
        LESSON_ICONS_HIGHTLIGHT[LESSON_VIDEOPLAYER] = R.drawable.videoplayer;
        LESSON_ICONS_HIGHTLIGHT[LESSON_ROTATABLE] = R.drawable.rotatable;
        LESSON_ICONS_HIGHTLIGHT[LESSON_SEEKBAR] = R.drawable.seekbar;
        LESSON_ICONS_HIGHTLIGHT[LESSON_GESTURE_DETECTOR] = R.drawable.gesturedec;
        LESSON_ICONS_HIGHTLIGHT[LESSON_PREFERENCE] = R.drawable.prference;
        LESSON_ICONS_HIGHTLIGHT[LESSON_BROADCAST_SERVICE] = R.drawable.broadcast_service;
        LESSON_ICONS_HIGHTLIGHT[LESSON_NET] = R.drawable.net;

    };
    private static final int[] LESSON_ICONS_NORMAL = new int[LESSON_NUM_ALL];
    static {
        LESSON_ICONS_NORMAL[LESSON_TABLE_LAYOUT] = R.drawable.tablelayout;
        LESSON_ICONS_NORMAL[LESSON_PAGES_SCROLLLAYOUT] = R.drawable.scrollpages;
        LESSON_ICONS_NORMAL[LESSON_ANIMATION] = R.drawable.animation;
        LESSON_ICONS_NORMAL[LESSON_VIDEOPLAYER] = R.drawable.videoplayer;
        LESSON_ICONS_NORMAL[LESSON_ROTATABLE] = R.drawable.rotatable;
        LESSON_ICONS_NORMAL[LESSON_SEEKBAR] = R.drawable.seekbar;
        LESSON_ICONS_NORMAL[LESSON_GESTURE_DETECTOR] = R.drawable.gesturedec;
        LESSON_ICONS_NORMAL[LESSON_PREFERENCE] = R.drawable.prference;
        LESSON_ICONS_NORMAL[LESSON_BROADCAST_SERVICE] = R.drawable.broadcast_service;
        LESSON_ICONS_NORMAL[LESSON_NET] = R.drawable.net;

    };

    private static final int[] LESSON_ICONS_TITLE_ID = new int[LESSON_NUM_ALL];
    static {
        LESSON_ICONS_TITLE_ID[LESSON_TABLE_LAYOUT] = R.string.icon_title_table_layout;
        LESSON_ICONS_TITLE_ID[LESSON_PAGES_SCROLLLAYOUT] = R.string.icon_title_scroll_pages;
        LESSON_ICONS_TITLE_ID[LESSON_ANIMATION] = R.string.icon_title_animation;
        LESSON_ICONS_TITLE_ID[LESSON_VIDEOPLAYER] = R.string.icon_title_video_player;
        LESSON_ICONS_TITLE_ID[LESSON_ROTATABLE] = R.string.icon_title_rotatable;
        LESSON_ICONS_TITLE_ID[LESSON_SEEKBAR] = R.string.icon_title_seekbar;
        LESSON_ICONS_TITLE_ID[LESSON_GESTURE_DETECTOR] = R.string.icon_title_gesture_detector;
        LESSON_ICONS_TITLE_ID[LESSON_PREFERENCE] = R.string.icon_title_preference;
        LESSON_ICONS_TITLE_ID[LESSON_BROADCAST_SERVICE] = R.string.icon_title_service_broadcast;
        LESSON_ICONS_TITLE_ID[LESSON_NET] = R.string.icon_title_net;
    };

    private static final String[] LESSON_HTML_FILE = new String[LESSON_NUM_ALL];
    static {
        LESSON_HTML_FILE[LESSON_TABLE_LAYOUT] = "lesson/html/lesson_table_layout.html";
        LESSON_HTML_FILE[LESSON_PAGES_SCROLLLAYOUT] = "lesson/html/lesson_pages_scrolllayout.html";
        LESSON_HTML_FILE[LESSON_ANIMATION] = "lesson/html/lesson_animation.html";
        LESSON_HTML_FILE[LESSON_VIDEOPLAYER] = "lesson/html/lesson_videoplayer.html";
        LESSON_HTML_FILE[LESSON_ROTATABLE] = "lesson/html/lesson_rotatable.html";
        LESSON_HTML_FILE[LESSON_SEEKBAR] = "lesson/html/lesson_seekbar.html";
        LESSON_HTML_FILE[LESSON_GESTURE_DETECTOR] = "lesson/html/lesson_gesture_detector.html";
        LESSON_HTML_FILE[LESSON_PREFERENCE] = "lesson/html/lesson_preference.html";
        LESSON_HTML_FILE[LESSON_BROADCAST_SERVICE] = "lesson/html/lesson_broadcast_service.html";
        LESSON_HTML_FILE[LESSON_NET] = "lesson/html/lesson_net.html";
    };

    private static final String[] LESSON_VIDEO_FILE = new String[LESSON_NUM_ALL];
    static {
        LESSON_VIDEO_FILE[LESSON_TABLE_LAYOUT] = "http://18ducxy.com/vids/lesson_tablelayout.mp4";
        LESSON_VIDEO_FILE[LESSON_PAGES_SCROLLLAYOUT] = "http://18ducxy.com/vids/lesson_pages_scroll.mp4";
        LESSON_VIDEO_FILE[LESSON_ANIMATION] = "http://18ducxy.com/vids/lesson_animation.mp4";
        LESSON_VIDEO_FILE[LESSON_VIDEOPLAYER] = "http://18ducxy.com/vids/lesson_video.mp4";
        LESSON_VIDEO_FILE[LESSON_ROTATABLE] = "http://18ducxy.com/vids/lesson_rotatable.mp4";
        LESSON_VIDEO_FILE[LESSON_SEEKBAR] = "http://18ducxy.com/vids/lesson_seekbar.mp4";
        LESSON_VIDEO_FILE[LESSON_GESTURE_DETECTOR] = "http://18ducxy.com/vids/lesson_gesture_detector.mp4";
        LESSON_VIDEO_FILE[LESSON_PREFERENCE] = "http://18ducxy.com/vids/lesson_preference.mp4";
        LESSON_VIDEO_FILE[LESSON_BROADCAST_SERVICE] = "http://18ducxy.com/vids/lesson_broadcast_service.mp4";
        LESSON_VIDEO_FILE[LESSON_NET] = "http://18ducxy.com/vids/lesson_net.mp4";
    };

    private DestopPagesLayout mLayout;
    private PageLayout[] mPagesLayoutArray;
    private XCRoundRectImageView[] mRotateImageViewsArray;

    ViewGroup.MarginLayoutParams mLayoutParaVG = new  ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT,
            ViewGroup.MarginLayoutParams.MATCH_PARENT);
    ViewGroup.MarginLayoutParams mLayoutParaVG2 = new  ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            ViewGroup.MarginLayoutParams.MATCH_PARENT);

    LinearLayout.LayoutParams mLayoutPara = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    @Override
    public void onClick(View v) {
        for (int i = 0; i < LESSON_NUM_ALL && mRotateImageViewsArray[i] != null; i++){
            if (v == mRotateImageViewsArray[i]){
                Intent intent = new Intent(getApplicationContext(), LessonActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("htmlFileName",LESSON_HTML_FILE[i]);
                bundle.putString("videoFilePath",LESSON_VIDEO_FILE[i]);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.destop);

        mLayout = (DestopPagesLayout)findViewById(R.id.mylayout);

        mRotateImageViewsArray = new XCRoundRectImageView[LESSON_NUM_ALL];
        mScreenNums = LESSON_NUM_ALL % NUM_ONE_SCREEN == 0? LESSON_NUM_ALL / NUM_ONE_SCREEN:LESSON_NUM_ALL / NUM_ONE_SCREEN +1;
        mPagesLayoutArray = new PageLayout[mScreenNums];

        for (int i = 0; i < mScreenNums; i++){
            PageLayout pageLayout = new PageLayout(this);
            // imgView.setImageResource(IMAGE_IDS[i]);
            //  pageLayout.setBackground(getResources().getDrawable(R.drawable.bg_scroll_background));

            mLayoutParaVG.setMargins(2, 2, 2, 2);
            pageLayout.setLayoutParams(mLayoutParaVG);
            mPagesLayoutArray[i] = pageLayout;

            for (int j = 0 ; j < NUM_ONE_SCREEN &&  i * NUM_ONE_SCREEN + j < LESSON_NUM_ALL; j++){
                XCRoundRectImageView rotateImageView = new XCRoundRectImageView(this, 0);
                // rotateImageView.setImageResource(IMAGE_IDS[j]);
                rotateImageView.setBackgroundColor(getResources().getColor(R.color.color10));
                // rotateImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_scroll_background));
                mLayoutPara.setMargins(2, 2, 2, 2);
                rotateImageView.setLayoutParams(mLayoutPara);
                rotateImageView.setPadding(MODE_DEFAULT_PADDING, MODE_DEFAULT_PADDING,
                        MODE_DEFAULT_PADDING, MODE_DEFAULT_PADDING);
                pageLayout.addView(rotateImageView);
                mRotateImageViewsArray[i * NUM_ONE_SCREEN + j] = rotateImageView;
                rotateImageView.setOnClickListener(this);
            }
            mLayout.addView(pageLayout);
        }

        for (int i = 0; i < LESSON_NUM_ALL; i++){
            mRotateImageViewsArray[i].setImageResource(LESSON_ICONS_HIGHTLIGHT[i]);
            mRotateImageViewsArray[i].setDrawTextTitle(getResources().getString(LESSON_ICONS_TITLE_ID[i]));
        }

        mPageControl = (PageIndictorLayout)findViewById(R.id.pageControl);
        mPageControl.bindScrollViewGroup(mLayout);

    }
}
