package calendar.yc.com.calendar.util;

import android.app.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by wanglin  on 2018/1/14 12:56.
 */

public class WeekUtil

{
    public static String num2Str(int number) {
        String date = "";
        switch (number) {
            case 1:
                date = "周日";
                break;
            case 2:
                date = "周一";
                break;
            case 3:
                date = "周二";
                break;
            case 4:
                date = "周三";
                break;
            case 5:
                date = "周四";
                break;
            case 6:
                date = "周五";
                break;
            case 7:
                date = "周六";
                break;

        }
        return date;
    }

    /**
     * 获取指定日期在本周的星期数
     *
     * @param pTime
     * @return
     */
    public static String assignDate2Week(String pTime) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            e.printStackTrace();
        }



        return num2Str(c.get(Calendar.DAY_OF_WEEK));

    }


    public static String digit2Ch(int num) {
        String ch = "";
        switch (num) {
            case 1:
                ch = "一";
                break;
            case 2:
                ch = "二";
                break;
            case 3:
                ch = "三";
                break;
            case 4:
                ch = "四";
                break;
        }

        return ch;
    }
}
