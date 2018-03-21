package yc.com.calendar.util;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.subutil.util.ThreadPoolUtils;
import com.blankj.utilcode.util.FileIOUtils;

import com.kk.utils.PathUtils;


/**
 * Created by wanglin  on 2018/1/23 10:52.
 */

public class SimpleCacheUtils {

    public static void writeCache(final Context context, final String key, final String json) {
        new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute(new Runnable() {
            @Override
            public void run() {
                String path = PathUtils.makeDir(context, "cache");
                FileIOUtils.writeFileFromString(path + "/" + key, json);
            }
        });
    }

    public static abstract class CacheRunnable implements Runnable {
        private String json;

        public void setJson(String json) {
            this.json = json;
        }

        public String getJson() {
            return json;
        }
    }

    public static void readCache(final Context context, final String key, final CacheRunnable runnable) {
        readCache(context, key, runnable, null);
    }

    public static void readCache(final Context context, final String key, final CacheRunnable srunnable, final
    CacheRunnable erunnable) {
        new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute(new Runnable() {
            @Override
            public void run() {
                String path = PathUtils.makeDir(context, "cache");
                String json = FileIOUtils.readFile2String(path + "/" + key);
                if (!TextUtils.isEmpty(json)) {
                    if (srunnable != null) {
                        srunnable.setJson(json);
                        srunnable.run();
                    }
                } else {
                    if (erunnable != null) {
                        erunnable.run();
                    }
                }
            }
        });
    }
}
