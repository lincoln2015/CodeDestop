package open.codedestop.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 ** Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/28.
 */
public class TwoStateImageView extends ImageView {
    private static final float DISABLED_ALPHA = 0.4f;
    private boolean mFilterEnabled = false;

    public TwoStateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoStateImageView(Context context) {
        this(context, null);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mFilterEnabled) {
            if (enabled) {
                setAlpha(1.0f);
            } else {
                setAlpha(DISABLED_ALPHA);
            }
        }
    }

    public void enableFilter(boolean enabled) {
        mFilterEnabled = enabled;
    }
}
