package calendar.yc.com.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.RxBus;
import com.vondear.rxtools.RxLogUtils;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/1/11 15:38.
 */

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        try {

            ButterKnife.bind(this, view);
        } catch (Exception e) {
            RxLogUtils.e(TAG, "--> 初始化失败..");
        }
        init();
        RxBus.get().register(this);
        return view;
    }

    protected abstract int getLayoutId();

    protected abstract void init();

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
