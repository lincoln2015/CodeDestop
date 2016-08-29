package open.codedestop.Util;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 ** Created by anxiagng.xiao (lincoln.shaw.wk@gmail.com) on 2016/8/29.
 */
public class DataUtil {
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context
                    .getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
