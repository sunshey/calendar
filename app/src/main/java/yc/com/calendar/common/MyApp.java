package yc.com.calendar.common;

import android.app.Application;
import android.os.Build;

import com.blankj.utilcode.util.Utils;
import com.kingja.loadsir.callback.ProgressCallback;
import com.kingja.loadsir.core.LoadSir;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.tencent.bugly.Bugly;
import com.umeng.commonsdk.UMConfigure;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import yc.com.calendar.callback.EmptyCallback;
import yc.com.calendar.callback.ErrorCallback;
import yc.com.calendar.callback.LoadingCallback;
import yc.com.calendar.util.DbManager;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2018/1/11 15:32.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        RxUtils.init(this);
//        Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                init();
//            }
//        });
        init();

    }

    private void init() {
        Utils.init(this);
        DbManager.initManager(getApplicationContext());
        Bugly.init(getApplicationContext(), "7e2f7f339a", false);

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);

        //全局信息初始化
        GoagalInfo.get().init(this);


        //设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = getSV();
        params.put("sv", sv);
        params.put("device_type", "2");
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }
        HttpConfig.setDefaultParams(params);

        ProgressCallback loadingCallback = new ProgressCallback.Builder()
                .build();

        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(loadingCallback)
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
    }

    public static String getSV() {
        return Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
    }

}
