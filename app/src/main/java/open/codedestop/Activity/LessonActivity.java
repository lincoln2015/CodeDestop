package open.codedestop.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import open.codedestop.R;

/**
 * Created by Administrator on 2016/8/29.
 */
public class LessonActivity extends AppCompatActivity {
    private final String TAG = "LessonActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lesson);

    }

}
