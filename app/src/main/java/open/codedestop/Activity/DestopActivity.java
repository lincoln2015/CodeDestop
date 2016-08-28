package open.codedestop.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import open.codedestop.Layout.DestopPagesLayout;
import open.codedestop.R;

/**
 ** Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/28.
 */
public class DestopActivity extends AppCompatActivity {
    private DestopPagesLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.destop);

        mLayout = (DestopPagesLayout)findViewById(R.id.mylayout);


    }
}
