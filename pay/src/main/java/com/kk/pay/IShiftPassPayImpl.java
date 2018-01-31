package com.kk.pay;

import android.app.Activity;

import com.kk.loading.LoadingDialog;
import com.kk.pay.other.LogUtil;
import com.kk.pay.other.ToastUtil;
import com.switfpass.pay.thread.NetHelper;
import com.switfpass.pay.utils.MD5;
import com.switfpass.pay.utils.SignUtils;
import com.switfpass.pay.utils.XmlUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhangkai on 2017/6/26.
 */

public class IShiftPassPayImpl extends IPayImpl {

    public static String url = "http://pay.ideanin.com/gateway/";

    public IShiftPassPayImpl(Activity context) {
        super(context);
        loadingDialog = new LoadingDialog(context);
//        isGen = true;
    }


    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }

        final PayInfo payInfo = orderInfo.getPayInfo();
        final Map<String, String> params = new HashMap<>();
        params.put("service", "pay.weixin.wappay");
        params.put("sign_type", "MD5");
        params.put("mch_id", payInfo.getMerchantID());//666777888001    904170629809
        params.put("out_trade_no", orderInfo.getOrder_sn());
        params.put("body", orderInfo.getName());

        BigDecimal price = new BigDecimal(orderInfo.getMoney() * 100 + "");

        params.put("total_fee", price.intValue() + "");
        params.put("notify_url", payInfo.getNotify_url());
        params.put("mch_app_name", "扬扬助手");
        params.put("mch_app_id", "com.kk.pay");
        params.put("nonce_str", randNonce());
        params.put("device_info", "AND_WAP");
        params.put("mch_create_ip", payInfo.getIp());
        params.put("callback_url", payInfo.getCallback_url());
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String sign = MD5.md5s(buf.toString() + "&key=" + payInfo.getKey()).toUpperCase();
        params.put("sign", sign);

        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.msg("params  " + XmlUtils.toXml(params));
                String result = NetHelper.getInstance().HttpPost(payInfo.getReturn_url(), XmlUtils.toXml(params));
                final HashMap mapResult = XmlUtils.parse(result);
                LogUtil.msg("result  " + result);
                if (mapResult != null && "0".equals(mapResult.get("status")) && "0".equals(mapResult.get("result_code"))) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WebPopupWindow webPopupWindow = new WebPopupWindow(mContext, mapResult.get("pay_info").toString(), orderInfo.getPayInfo().getIs_override_url() == 1);
                                webPopupWindow.show(mContext.getWindow().getDecorView().getRootView());
//
                                IPayImpl.uiPayCallback = iPayCallback;
                                IPayImpl.uOrderInfo = orderInfo;
                                isGen = true;
                            } catch (Exception e) {
                                ToastUtil.toast2(mContext, "支付错误");
                                LogUtil.msg("Exception  " + e.getMessage());
                            }
                        }
                    });
                }
            }
        }).start();


    }

    private String randNonce() {
        return new Random().nextInt(1000000000) + "";
    }


}