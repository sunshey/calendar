package com.kk.pay;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.kk.loading.LoadingDialog;
import com.kk.pay.other.JudgeUtil;
import com.kk.pay.other.LogUtil;
import com.kk.pay.other.ToastUtil;

import java.net.URLEncoder;

/**
 * Created by wanglin  on 2017/8/30 10:45.
 */

public class IDunXingH5PayImpl extends IPayImpl {
    private String mType;

    public IDunXingH5PayImpl(Activity context, String type) {
        super(context);
        this.mType = type;
        loadingDialog = new LoadingDialog(context);
    }

    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }

        IPayImpl.uiPayCallback = iPayCallback;
        IPayImpl.uOrderInfo = orderInfo;
        isGen = true;

        PayInfo payInfo = orderInfo.getPayInfo();
        String url = payInfo.getPayurl();
        try {

            if (!TextUtils.isEmpty(payInfo.getRemarks())) {
                String remarks = payInfo.getRemarks();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(url).append("&remarks=").append(URLEncoder.encode(remarks, "gb2312"));
                url = stringBuilder.toString();
            }

            if (orderInfo.getPayway().contains("alipay")) {
                if (!JudgeUtil.checkAliPayInstalled(mContext)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(intent);
                    return;
                }
            }


            WebPopupWindow webPopupWindow = new WebPopupWindow(mContext, url, payInfo.getIs_override_url() == 1);
            webPopupWindow.show(mContext.getWindow().getDecorView().getRootView());

        } catch (Exception e) {
            ToastUtil.toast2(mContext, "支付错误");
            LogUtil.msg("Exception  " + e.getMessage());
        }

    }

}
