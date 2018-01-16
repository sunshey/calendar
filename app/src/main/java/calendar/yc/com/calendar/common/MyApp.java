package calendar.yc.com.calendar.common;

import android.app.Application;

import com.vondear.rxtools.RxUtils;

import calendar.yc.com.calendar.util.DbManager;

/**
 * Created by wanglin  on 2018/1/11 15:32.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RxUtils.init(this);
        DbManager.initManager(getApplicationContext());


    }
}
