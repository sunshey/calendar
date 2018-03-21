package yc.com.calendar.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;


import com.hwangjr.rxbus.RxBus;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.ProgressCallback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kk.pay.other.LogUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import yc.com.calendar.R;
import yc.com.calendar.callback.EmptyCallback;
import yc.com.calendar.callback.TimeoutCallback;

/**
 * Created by wanglin  on 2018/1/11 15:29.
 */

public abstract class BaseActivity extends AppCompatActivity {


    protected LoadService loadService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置全屏

        setContentView(getLayoutId());
//        addStatusViewWithColor(this, getResources().getColor(R.color.red_d03f3f));
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            LogUtils.e("--> 初始化失败");
        }
        RxBus.get().register(this);
        init();
        if (isShowLoading()) {
            showState();
            getData();
        }
    }

    protected abstract int getLayoutId();

    protected abstract void init();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }


    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private void addStatusViewWithColor(Activity activity, int color) {
        //设置 paddingTop
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, 0, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色

            activity.getWindow().setStatusBarColor(color);
        } else {
            //根布局添加占位状态栏
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight());
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, lp);

        }
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */

    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void showState() {
        ProgressCallback loadingCallback = new ProgressCallback.Builder()
                .setTitle("正在获取数据...", R.style.Hint_Title)
//                .setAboveSuccess(true)// attach loadingView above successView
                .build();

        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(loadingCallback)
                .addCallback(new EmptyCallback())
                .addCallback(new TimeoutCallback())
                .setDefaultCallback(ProgressCallback.class)
                .build();

        loadService = loadSir.register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(ProgressCallback.class);
                getData();

            }
        });
    }

    protected abstract void getData();

    protected abstract boolean isShowLoading();

}
