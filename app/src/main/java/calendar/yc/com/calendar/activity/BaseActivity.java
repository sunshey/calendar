package calendar.yc.com.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.hwangjr.rxbus.RxBus;
import com.vondear.rxtools.RxLogUtils;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/1/11 15:29.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            RxLogUtils.e("--> 初始化失败");
        }
        RxBus.get().register(this);
        init();
    }

    protected abstract int getLayoutId();

    protected abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
