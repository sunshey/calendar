package com.kk.pay.other;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

/**
 * 日志输出类
 * 
 * @author zhangkai
 * 
 */
public class LogUtil {
	private static final boolean DEBUG = true;

    public static final int LEVEL = 2;// 日志输出级别
    public static final int V = 0;
    public static final int D = 1;
    public static final int I = 2;
    public static final int W = 3;
    public static final int E = 4;

	private static final String TAG = "多开助手";

	public static void msg(String msg, int level) {

		switch (level) {
		case V:
			LogUtils.v(msg);
			break;
		case D:
            LogUtils.d(msg);
			break;
		case I:
            LogUtils.i(msg);
			break;
		case W:
            LogUtils.w(msg);
			break;
		case E:
            LogUtils.e(msg);
			break;
		default:
			break;
		}
	}

    public static void msg(String msg) {

        switch (LEVEL) {
            case V:
                Log.v(TAG, msg);
                break;
            case D:
                Log.d(TAG, msg);
                break;
            case I:
                Log.i(TAG, msg);
                break;
            case W:
                Log.w(TAG, msg);
                break;
            case E:
                Log.e(TAG, msg);
                break;
            default:
                break;
        }
    }
}
