package open.codedestop.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import open.codedestop.R;

/**
 ** Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/29.
 */
public class ShowPicActivity extends Activity {

    //private DisplayMetrics dm;
    private String TAG = "ShowPicActivity_xax";
    private final int UPDATE_IMAGE = 0x01;
    int dwidth;
    int dheight;
    private int mOrientation;

    private LinearLayout.LayoutParams params;
    public ProgressBar mProgressBar;
    private ImageView mView;
    private String mSource;
    private Bitmap mBitmap;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_IMAGE:
                    Log.i(TAG, "update image");
                    mProgressBar.setVisibility(View.GONE);
                    mView.setVisibility(ImageView.VISIBLE);
                    mView.setImageBitmap(mBitmap);
                    mView.setLayoutParams(params);
                    ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
                    layout.removeAllViews();
                    layout.addView(mView);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.show_picture);
        // Show the Up button in the action bar.
        setupActionBar();

        mSource = getIntent().getExtras().getString("picUrl");
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        mView = (ImageView)findViewById(R.id.BigImage);
        mProgressBar = (ProgressBar)findViewById(R.id.leadProgressBar);
        mView.setVisibility(ImageView.GONE);

        Display display = getWindowManager().getDefaultDisplay();
        dwidth = display.getWidth();
        dheight = display.getHeight();

        if (dwidth > dheight) {
            mOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //横屏
        }else{
            mOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;  //竖屏
        }
        Log.i(TAG, "orientation:" + mOrientation+",dwidth:"+dwidth+",dheight:"+dheight);

        new Thread(new Runnable(){
            public void run(){
                Log.i(TAG, "Thread");
                mBitmap = reSizePicture(mSource);
                try{
                    Thread.sleep(0);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                Log.i(TAG, "Thread post");
                // remove waitting ,just show bitmap
                /*
                view.post(new Runnable(){
                    public void run(){
                        Log.i(TAG, "Thread post show");
                        progressBar.setVisibility(View.GONE);
                        view.setVisibility(ImageView.VISIBLE);
                        view.setImageBitmap(bitmap);
                        view.setLayoutParams(params);
                        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
                        layout.removeAllViews();
                        layout.addView(view);
                    }
                });*/
                Message msg = new Message();
                msg.what = UPDATE_IMAGE;
                handler.sendMessage(msg);

            }
        }).start();

        mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent i = new Intent(ShowPicActivity.this, LessonActivity.class);
                // startActivity(i);
                finish();
            }
        });

    }

    public Bitmap reSizePicture(String path){
        Bitmap resizedBitmap = null;
        Bitmap bitmapOrg = null;
        try{
            InputStream a = getResources().getAssets().open(path);
            bitmapOrg = BitmapFactory.decodeStream(a);

            Log.i(TAG, "bitmapOrg");
            int width = bitmapOrg.getWidth();
            int height = bitmapOrg.getHeight();


            int newWidth = dwidth;
            int newHeight = dwidth*bitmapOrg.getHeight()/bitmapOrg.getWidth() ;

            if (newWidth > width && newHeight > height) {
                // calculate the scale - in this case = 0.4f
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;

                // createa matrix for the manipulation
                Matrix matrix = new Matrix();
                // resize the bit map
                matrix.postScale(scaleWidth, scaleHeight);

                resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
                        height, matrix, true);
            } else {
                resizedBitmap = bitmapOrg;
            }
            Log.i(TAG, "resizedBitmap");
        }catch(MalformedURLException e1){
            e1.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return resizedBitmap;

    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.setRequestedOrientation(mOrientation);
    }

    protected void onResume() {
        mOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
        this.setRequestedOrientation(mOrientation);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        if (width > height) {
            mOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            mOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        super.onResume();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //  getActionBar().setDisplayHomeAsUpEnabled(true); //remove temply
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_pic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}