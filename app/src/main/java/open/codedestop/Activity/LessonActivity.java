package open.codedestop.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import open.codedestop.Layout.ScrollViewContainer;
import open.codedestop.R;
import open.codedestop.Util.DataUtil;
import open.codedestop.Util.LessonAction;
import open.codedestop.Util.LinkMovementMethodExt;
import open.codedestop.Util.MImageGetter;
import open.codedestop.Util.MTagHandler;
import open.codedestop.Util.MessageSpan;

/**
 * Created by anxiang.xiao on 2016/7/25.
 */
public class LessonActivity  extends AppCompatActivity implements View.OnClickListener ,LessonAction{

    private final String TAG = "LessonActivity";
    private TextView text;
    private WebView mWebView;
    private LinearLayout mWebviewContainer;
    private ProgressBar mProgressBar;
    private ProgressBar mProgressBarVideo;
    private ImageView mPlayBtn;
    private ImageView mStopBtn;
    private ImageView mPlayBtnBiger;
    private Button mPlayB;
    private Context c;

    private SurfaceView mSurface1;
    private MediaPlayer mediaPlayer1;
    private int mPostion = 0;
    private SeekBar mSeekBar;
    private int mProgress = 0;
    private final static int UPDATE_PROGRESS = 0x1;
    private Thread mThread;
    private String mVideoFilePath="";
    private boolean mThreadDestroy = false;

    ScrollView mScrollView;
    ScrollViewContainer mScrollViewContailner;

    // for scroll back main activity start
    private  int x = 0;
    private  int y = 0;

    private int downX = 0;
    private int downY = 0;

    private int lastX = downX;
    private int lastY = downY;

    private int mLastUpPostion = 0;
    private int mCurrentPage = 0;

    private boolean mIsLongPress = false;

    private int mCount ;
    private int mWidth ;
    private int mHeight ;

    // for scroll back main activity end


    @Override
    public void onExitLesson() {
        this.finish();
    }



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            mProgress = (int)(((float)mediaPlayer1.getCurrentPosition()  /  (float)mediaPlayer1.getDuration()) * 100);
            // super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_PROGRESS:
                    Log.i(TAG, "currentProgress" + mProgress);
                    mSeekBar.setProgress(mProgress);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v == mPlayBtn){
            Log.i(TAG, "btn_playBtn");
            if (!mediaPlayer1.isPlaying())
                mediaPlayer1.start();
            mPlayBtn.setVisibility(View.GONE);
            mStopBtn.setVisibility(View.VISIBLE);
        }else if (v == mStopBtn){
            Log.i(TAG, "btn_stopBtn");
            if (mediaPlayer1.isPlaying())
                mediaPlayer1.pause();

            mPlayBtn.setVisibility(View.VISIBLE);
            mStopBtn.setVisibility(View.GONE);
        }else if (v == mPlayBtnBiger){
            Log.i(TAG, "btn_playBtnBiger");
            try {
                play();
                mSeekBar.setProgress(0);
                mProgressBarVideo.setVisibility(View.VISIBLE);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mProgressBarVideo.setVisibility(View.VISIBLE);
            mPlayBtnBiger.setVisibility(View.GONE);
        }
    }

    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {
        mediaPlayer1.reset();
        mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // String path = "android.resource://xax.mediaplayer10/"+R.raw.bmw;
        // Log.i(TAG, "source path:" + path);
        // mediaPlayer1.setDataSource("/storage/sdcard0/demo7.mp4");
        mediaPlayer1.setDataSource(mVideoFilePath);
        // mediaPlayer1.setDataSource(path);
        //  mediaPlayer1.setDataSource(this, Uri.parse(path));
        // 把视频输出到SurfaceView上
        mediaPlayer1.setDisplay(mSurface1.getHolder());
        mediaPlayer1.prepareAsync();
        //mediaPlayer1.start();
        mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //Toast.makeText(getApplicationContext(), "onPrepared_ok", Toast.LENGTH_SHORT).show();
                mProgressBarVideo.setVisibility(View.GONE);
                mp.start();

                mPlayBtn.setVisibility(View.GONE);
                mStopBtn.setVisibility(View.VISIBLE);
            }
        });

        mediaPlayer1.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // Toast.makeText(getApplicationContext(), "what:"+what+",extra:"+extra, Toast.LENGTH_SHORT).show();
                switch (what) {
                    case -1004:
                        Log.d(TAG, "MEDIA_ERROR_IO");
                        break;
                    case -1007:
                        Log.d(TAG, "MEDIA_ERROR_MALFORMED");
                        break;
                    case 200:
                        Log.d(TAG, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                        break;
                    case 100:
                        Log.d(TAG, "MEDIA_ERROR_SERVER_DIED");
                        break;
                    case -110:
                        Log.d(TAG, "MEDIA_ERROR_TIMED_OUT");
                        break;
                    case 1:
                        Log.d(TAG, "MEDIA_ERROR_UNKNOWN");
                        break;
                    case -1010:
                        Log.d(TAG, "MEDIA_ERROR_UNSUPPORTED");
                        break;
                }
                switch (extra) {
                    case 800:
                        Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING");
                        break;
                    case 702:
                        Log.d(TAG, "MEDIA_INFO_BUFFERING_END");
                        break;
                    case 701:
                        Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case 802:
                        Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case 801:
                        Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE");
                        break;
                    case 1:
                        Log.d(TAG, "MEDIA_INFO_UNKNOWN");
                        break;
                    case 3:
                        Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START");
                        break;
                    case 700:
                        Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                        break;
                }
                return false;
            }
        });
    }

    private class SurfaceViewLis implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer1.setDisplay(mSurface1.getHolder());
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    public void doAction(){
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        //hide the status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.lesson);

        mPlayBtn = (ImageView)findViewById(R.id.play);
        mStopBtn = (ImageView)findViewById(R.id.stop);
        mPlayBtnBiger = (ImageView)findViewById(R.id.play_biger);
        mScrollView =  (ScrollView)findViewById(R.id.scroll);
        mScrollViewContailner = (ScrollViewContainer)findViewById(R.id.scrollContainer);

        text = (TextView)findViewById(R.id.text);
        mWebView = (WebView)findViewById(R.id.noteView);
        mWebviewContainer = (LinearLayout)findViewById(R.id.webviewContainer);
        c = this.getApplicationContext();

        Bundle bundle = getIntent().getExtras();
        String htmlFileName = bundle.getString("htmlFileName");
        mVideoFilePath = bundle.getString("videoFilePath");
        Log.v("htmlFileName=", htmlFileName);
        String artAdress = htmlFileName;
        String artImg =  "lesson/imgs/"; //keep temply, this para should be pass from DestopActivity  later

        mProgressBar = (ProgressBar)findViewById(R.id.leadProgressBar);
        mProgressBarVideo  = (ProgressBar)findViewById(R.id.leadProgressBarVideo);
        text.setVisibility(View.GONE);
        mWebviewContainer.setVisibility(View.GONE);
        mWebView.setVisibility(View.GONE);

        ArtUpdater au = new ArtUpdater(artAdress, artImg);
        au.execute();

        mSurface1 = (SurfaceView) findViewById(R.id.surface1);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mediaPlayer1 = new MediaPlayer();
        //设置播放时打开屏幕
        mSurface1.getHolder().setKeepScreenOn(true);
        mSurface1.getHolder().addCallback(new SurfaceViewLis());

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mThreadDestroy){
                    // Log.i(TAG, "Thread run, isplaying:"+mediaPlayer1.isPlaying());
                    // remove temply
                    if (mediaPlayer1.isPlaying()){
                        Message msg = new Message();
                        msg.what = UPDATE_PROGRESS;
                        Log.i(TAG, "Thread run");
                        handler.sendMessage(msg);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        mThread.start();

        mPlayBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
        mPlayBtnBiger.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "onProgressChanged:" + progress + ",fromUser:" + fromUser);
                if (fromUser) {
                    mediaPlayer1.seekTo(mediaPlayer1.getDuration() * progress / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "onStopTrackingTouch");
            }
        });

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int width = v.getMeasuredWidth();

                Log.i(TAG, "onTouch, X:" + x + ",Y:" + y);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
                        lastX = downX;
                        lastY = downY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        downX = mScrollViewContailner.getDownX();
                        downY =  mScrollViewContailner.getDownY();
                        Log.i(TAG, "abs x:" + Math.abs(lastX - downX) + ",abs y:" + Math.abs(lastY - downY) + ", width_2:" + width / 2 + ",width_3:" + width / 3);
                        if (Math.abs(lastX - downX) > width / 2 && Math.abs(lastY - downY) < width / 3) {
                            finish();
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    class ArtUpdater extends AsyncTask<Void, Void, Void> {

        String artAdresss;
        String artImg;
        //HttpGet req;
        String htmlContent;
        //HttpClient httpClient = new DefaultHttpClient();

        public ArtUpdater(String artAdresss, String artImg) {
            this.artAdresss  = artAdresss;
            this.artImg = artImg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String htmlString = null;

                htmlString = DataUtil.getFromAssets(LessonActivity.this, artAdresss);

                htmlContent = htmlString;

            } catch (Exception e) {
                return null;
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            if (htmlContent != null) {

                htmlContent = htmlContent.replace("<img src=\"imgs/", "<br><img src=\"" + artImg);

                htmlContent = htmlContent.replaceAll("<head>([\\s\\S]*)<\\/head>","");

                if (htmlContent.contains("><p>")) {
                    String regularExpression1 = "(<[^\\/]\\w><p>)";
                    Log.i(TAG, "regularExpression1:"+regularExpression1);
                    Pattern pat1 = Pattern.compile(regularExpression1);
                    Matcher mat1 = pat1.matcher(htmlContent);
                    if(mat1.find()){
                        for (int i = 0; i < mat1.groupCount(); i++) {
                            System.out.println("@@@"+mat1.group(i));
                            String temp = mat1.group(i).replace("<p>", "");
                            htmlContent = htmlContent.replace(mat1.group(i), temp);
                            String tail = temp.replace("<", "</");
                            htmlContent = htmlContent.replace("</p>"+tail, tail);
                            System.out.println(htmlContent);
                        }
                    }

                }

                if (htmlContent.contains("<pre>")) {
                    String regularExpression12 = "<pre>(.+?)</pre>";
                    Log.i(TAG, "regularExpression12:"+regularExpression12);
                    Pattern pat12 = Pattern.compile(regularExpression12);
                    Matcher mat12 = pat12.matcher(htmlContent);
                    if(mat12.find()){
                        for (int i = 0; i < mat12.groupCount(); i++) {
                            Log.i(TAG, "regularExpression12:mat12.group(i)"+mat12.group(i));
                            System.out.println("@@@" + mat12.group(i));
                            String temp = mat12.group(i).replace("<pre>", "").replace("</pre>", "");
                            temp = temp.replaceAll("\n", "<br>").replaceAll(" ", "&nbsp;");
                            Log.i(TAG, "regularExpression12:temp:"+temp);
                            htmlContent = htmlContent.replace(mat12.group(i), temp);
                            // String tail = temp.replace("<", "</");
                            // htmlContent = htmlContent.replace("</p>"+tail, tail);
                            System.out.println(htmlContent);
                        }
                    }
                }
            }
            try{
                mProgressBar.setVisibility(View.GONE);
                text.setText(Html.fromHtml(htmlContent, new MImageGetter(text, LessonActivity.this), new MTagHandler()));

                Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        int what = msg.what;
                        if (what == 200) {
                            MessageSpan ms = (MessageSpan) msg.obj;
                            Object[] spans = (Object[])ms.getObj();

                            for (Object span : spans) {
                                if (span instanceof ImageSpan) {

                                    Intent intent = new Intent(c, ShowPicActivity.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putString("picUrl",((ImageSpan) span).getSource());
                                    intent.putExtras(bundle);

                                    startActivity(intent);
                                }
                            }
                        }
                    };
                };
                text.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));
                text.setVisibility(View.VISIBLE);
            } catch (Throwable e){
                mProgressBar.setVisibility(View.GONE);
                mWebviewContainer.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(artAdresss);
                if(e != null){
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        if (!mediaPlayer1.isPlaying()) {
            // 保存当前播放的位置
            Log.i(TAG, "onResume start");
            mPostion = mediaPlayer1.getCurrentPosition();
            // mediaPlayer1.start();
            if (mPlayBtnBiger.getVisibility() == View.GONE){
                mPlayBtn.setVisibility(View.VISIBLE);
                mStopBtn.setVisibility(View.GONE);
            }else{
                mPlayBtn.setVisibility(View.INVISIBLE);
                mStopBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        if (mediaPlayer1.isPlaying()) {
            // 保存当前播放的位置
            mPostion = mediaPlayer1.getCurrentPosition();
            mediaPlayer1.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        mThreadDestroy = true;

        if (mediaPlayer1.isPlaying())
            mediaPlayer1.stop();
        mediaPlayer1.release();

        super.onDestroy();
    }
}
