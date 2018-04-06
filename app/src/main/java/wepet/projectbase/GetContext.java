package wepet.projectbase;

import android.app.Application;
import android.content.Context;

/**
 * Created by coco on 2016-07-25.
 */
public class GetContext extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static  Context getContext(){
        return mContext;
    }
}
