package open.codedestop.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import open.codedestop.R;

/**
 * Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/28.
 * use with the DestoPagesLayout
 */
public class PageIndictorLayout extends LinearLayout {
    private Context mContext;
    private int mCount;

    public void bindScrollViewGroup(DestopPagesLayout scrollViewGroup) {
        this.mCount=scrollViewGroup.getChildCount();
        System.out.println("count="+mCount);
        generatePageControl(scrollViewGroup.getCurrentPage());

        scrollViewGroup.setOnScreenChangeListener(new DestopPagesLayout.OnScreenChangeListener() {

            public void onScreenChange(int currentIndex) {
                // TODO Auto-generated method stub
                generatePageControl(currentIndex);
            }
        });
    }

    public PageIndictorLayout(Context context) {
        super(context);
        this.init(context);
    }
    public PageIndictorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        this.mContext=context;
    }

    private void generatePageControl(int currentIndex) {
        this.removeAllViews();

        int pageNum = 4;
        int pageNo = currentIndex+1;
        int pageSum = this.mCount;

        if(pageSum>1){
            int currentNum = (pageNo % pageNum == 0 ? (pageNo / pageNum) - 1
                    : (int) (pageNo / pageNum))
                    * pageNum;
            if (currentNum < 0)
                currentNum = 0;
            if (pageNo > pageNum){
                ImageView imageView = new ImageView(mContext);
                imageView.setImageResource(R.drawable.rgk_zuo);
                this.addView(imageView);
            }

            for (int i = 0; i < pageNum; i++) {
                if ((currentNum + i + 1) > pageSum || pageSum < 2)
                    break;
                ImageView imageView = new ImageView(mContext);
                if(currentNum + i + 1 == pageNo){
                    imageView.setImageResource(R.drawable.rgk_page_indicator_focused);
                }else{
                    imageView.setImageResource(R.drawable.rgk_page_indicator);
                }
                this.addView(imageView);
            }
            if (pageSum > (currentNum + pageNum)) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageResource(R.drawable.rgk_you);
                this.addView(imageView);
            }
        }
    }
}