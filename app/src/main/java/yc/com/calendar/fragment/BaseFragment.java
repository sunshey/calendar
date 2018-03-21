package yc.com.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

/**
 * Created by wanglin  on 2018/1/11 15:38.
 */

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected LoadService loadService;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RxBus.get().register(this);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        try {
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            LogUtils.e(TAG, "--> 初始化失败..");
        }
        init();

        if (isShowLoading()) {
            showState();
        }
        getData();

        return rootView;


    }

    protected abstract int getLayoutId();

    protected abstract void init();


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    private void showState() {
        ProgressCallback loadingCallback = new ProgressCallback.Builder()
                .setTitle("正在获取数据...", R.style.Hint_Title)
//                .setAboveSuccess(true)// attach loadingView above successView
                .build();

        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(loadingCallback)
                .addCallback(new EmptyCallback())
                .setDefaultCallback(ProgressCallback.class)
                .build();

        loadService = loadSir.register(getActivity(), new Callback.OnReloadListener() {
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
