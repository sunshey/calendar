package yc.com.calendar.util;

import android.widget.ImageView;

import yc.com.calendar.R;

/**
 * Created by wanglin  on 2018/1/15 18:21.
 */

public class LunCalendarUtils {


    public static void setLunCalendarImage1(String name, ImageView imageView) {
        if (name.equals("正")) {
            imageView.setImageResource(R.mipmap.fz_zheng);
        } else if (name.equals("二")) {
            imageView.setImageResource(R.mipmap.fz_er);
        } else if (name.equals("三")) {
            imageView.setImageResource(R.mipmap.fz_san);
        } else if (name.equals("四")) {
            imageView.setImageResource(R.mipmap.fz_si);
        } else if (name.equals("五")) {
            imageView.setImageResource(R.mipmap.fz_wu);
        } else if (name.equals("六")) {
            imageView.setImageResource(R.mipmap.fz_liu);
        } else if (name.equals("七")) {
            imageView.setImageResource(R.mipmap.fz_qi);
        } else if (name.equals("八")) {
            imageView.setImageResource(R.mipmap.fz_ba);
        } else if (name.equals("九")) {
            imageView.setImageResource(R.mipmap.fz_jiu);
        } else if (name.equals("十")) {
            imageView.setImageResource(R.mipmap.fz_shi);
        } else if (name.equals("冬")) {
            imageView.setImageResource(R.mipmap.fz_dong);
        } else if (name.equals("腊")) {
            imageView.setImageResource(R.mipmap.fz_la);
        }
    }

    public static void setLunCalendarImage2(String name, ImageView imageView) {
        if (name.equals("初")) {
            imageView.setImageResource(R.mipmap.fz_chu);
        } else if (name.equals("十")) {
            imageView.setImageResource(R.mipmap.fz_shi);
        } else if (name.equals("二")) {
            imageView.setImageResource(R.mipmap.fz_er);
        } else if (name.equals("廿")) {
            imageView.setImageResource(R.mipmap.fz_nian);
        } else if (name.equals("三")) {
            imageView.setImageResource(R.mipmap.fz_san);
        }
    }


    public static void setLunCalendarImage3(String name, ImageView imageView) {
        if (name.equals("一")) {
            imageView.setImageResource(R.mipmap.fz_yi);
        } else if (name.equals("二")) {
            imageView.setImageResource(R.mipmap.fz_er);
        } else if (name.equals("三")) {
            imageView.setImageResource(R.mipmap.fz_san);
        } else if (name.equals("廿")) {
            imageView.setImageResource(R.mipmap.fz_nian);
        } else if (name.equals("四")) {
            imageView.setImageResource(R.mipmap.fz_si);
        } else if (name.equals("五")) {
            imageView.setImageResource(R.mipmap.fz_wu);
        } else if (name.equals("六")) {
            imageView.setImageResource(R.mipmap.fz_liu);
        } else if (name.equals("七")) {
            imageView.setImageResource(R.mipmap.fz_qi);
        } else if (name.equals("八")) {
            imageView.setImageResource(R.mipmap.fz_ba);
        } else if (name.equals("九")) {
            imageView.setImageResource(R.mipmap.fz_jiu);
        } else if (name.equals("十")) {
            imageView.setImageResource(R.mipmap.fz_shi);
        }
    }
}
