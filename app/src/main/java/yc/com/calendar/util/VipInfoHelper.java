package yc.com.calendar.util;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;

import java.util.List;

import yc.com.calendar.bean.VipInfo;
import yc.com.calendar.bean.VipInfoWrapper;
import yc.com.calendar.constants.SpConstant;

/**
 * Created by wanglin  on 2018/1/18 12:47.
 */

public class VipInfoHelper {
    private static VipInfoWrapper mVipInfoWrapper;
    private static List<VipInfo> mGoodInfoList;

    public static List<VipInfo> getGoodInfoList() {
        if (mGoodInfoList != null) {
            return mGoodInfoList;
        }
        String str = SPUtils.getInstance().getString(SpConstant.VIP_INFO);
        try {
            mGoodInfoList = JSON.parseArray(str, VipInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        return mGoodInfoList;
    }

    public static void setGoodInfoList(List<VipInfo> goodInfoList) {
        try {

            String json = JSON.toJSONString(goodInfoList);
            SPUtils.getInstance().put(SpConstant.VIP_INFO, json);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        mGoodInfoList = goodInfoList;
    }

    public static VipInfoWrapper getVipInfo() {
        if (mVipInfoWrapper != null) {
            return mVipInfoWrapper;
        }
        String str = SPUtils.getInstance().getString(SpConstant.VIP_INFO);
        try {
            mVipInfoWrapper = JSON.parseObject(str, VipInfoWrapper.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }

        return mVipInfoWrapper;
    }

    public static void setGoodInfoWrapper(VipInfoWrapper goodInfoWrapper) {
        try {

            String json = JSON.toJSONString(goodInfoWrapper);
            SPUtils.getInstance().put(SpConstant.VIP_INFO, json);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        mVipInfoWrapper = goodInfoWrapper;
    }

}
