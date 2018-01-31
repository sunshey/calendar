package yc.com.calendar.util;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;

import java.util.List;

import yc.com.calendar.bean.PayWayInfo;
import yc.com.calendar.constants.SpConstant;

/**
 * Created by wanglin  on 2018/1/18 12:47.
 */

public class PaywayInfoHelper {

    private static List<PayWayInfo> mPayWayInfoList;

    public static List<PayWayInfo> getPayWayInfoList() {
        if (mPayWayInfoList != null) {
            return mPayWayInfoList;
        }
        String str = SPUtils.getInstance().getString(SpConstant.PAY_WAY_INFO);
        try {
            mPayWayInfoList = JSON.parseArray(str, PayWayInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        return mPayWayInfoList;
    }

    public static void setPayWayInfoList(List<PayWayInfo> goodInfoList) {
        try {

            String json = JSON.toJSONString(goodInfoList);
            SPUtils.getInstance().put(SpConstant.PAY_WAY_INFO, json);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        mPayWayInfoList = goodInfoList;
    }


}
