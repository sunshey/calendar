package yc.com.calendar.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import yc.com.calendar.R;
import yc.com.calendar.engine.PaywayInfoEngine;

/**
 * Created by wanglin  on 2018/1/18 16:40.
 */

public abstract class BaseDialog extends Dialog {
    public Activity mContext;

    private PaywayInfoEngine paywayInfoEngine;

    public BaseDialog(@NonNull Activity context) {
        super(context, R.style.payDialog);
        this.mContext = context;
        paywayInfoEngine = new PaywayInfoEngine(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(getLayoutId(), null);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.75); // 高度设置为屏幕的0.7
        dialogWindow.setAttributes(lp);

        getView(view);

    }

    protected abstract void getView(View view);


    protected abstract int getLayoutId();








}
