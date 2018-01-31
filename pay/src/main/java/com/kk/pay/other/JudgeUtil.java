package com.kk.pay.other;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by wanglin  on 2017/11/22 16:14.
 */

public class JudgeUtil {

    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    public static boolean checkWxPayInstalled(Context context) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

        return msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
    }
}
