package calendar.yc.com.calendar.common;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.commonsdk.UMConfigure;
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
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);

    }
}
