package calendar.yc.com.calendar.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wanglin  on 2018/1/15 17:48.
 */

public class DateUtils {

    //  星期
    private static String[] week = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    //  农历月份
    private static String[] lunarMonth = {"正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月"};

    //  农历日
    private static String[] lunarDay = {"初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};


    private static String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
            "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};

    private static String[] years = new String[]{
            "属鼠", "属牛", "属虎", "属兔",
            "属龙", "属蛇", "属马", "属羊",
            "属猴", "属鸡", "属狗", "属猪"
    };

    private static String[] Gan = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛",
            "壬", "癸"};
    private static String[] Zhi = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未",
            "申", "酉", "戌", "亥"};

    /**
     * 获得当天time点时间戳
     */
    public static long getSignTime(int time) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, time);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() / 1000);
    }

    /**
     * 时间段格式化 hh:mm:ss 用来做倒计时
     */
    public static String timeFormat(long time) {
        int hours = (int) time / 3600;
        String hourStr;
        if (hours < 10) {
            hourStr = "0" + hours;

        } else {
            hourStr = hours + "";
        }
        int min = (int) (time - hours * 3600) / 60;
        String minStr;
        if (min < 10) {
            minStr = "0" + min;

        } else {
            minStr = min + "";
        }
        int second = (int) (time - (time / 60) * 60);
        String secondStr;
        if (second < 10) {
            secondStr = "0" + second;

        } else {
            secondStr = second + "";
        }
        String timeStr = (hourStr + ":" + minStr + ":" + secondStr);

        return timeStr;
    }

    /**
     * 获取年月日 格式yyyy-MM-dd
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 获取年、月 格式 yyyy-MM
     *
     * @return
     */
    public static String getMonth() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String month = simpleDateFormat.format(date);
        return month;
    }

    /**
     * 获取当月日期
     *
     * @return Day of month
     */
    public static String getDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day + "";
    }

    /**
     * 获取星期几
     */
    public static String getWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return week[dayOfWeek - 1];
    }

    /**
     * 获取农历月份
     *
     * @return
     */
    public static String getLunarMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int[] lunarDate = LunarCalendar.solarToLunar(year, month, day);
        return lunarMonth[lunarDate[1] - 1];
    }

    /**
     * 获取农历日
     *
     * @return
     */
    public static String getLunarDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int[] lunarDate = LunarCalendar.solarToLunar(year, month, day);
        return lunarDay[lunarDate[2] - 1];
    }


    /**
     * 获取当前阴历
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getCurrentLunar(int year, int month, int day) {
        int[] lunarDate = LunarCalendar.solarToLunar(year, month, day);

        return lunarMonth[lunarDate[1] - 1] + lunarDay[lunarDate[2] - 1];
    }


    /**
     * 获取星座
     *
     * @param month
     * @param day
     * @return
     */
    public static String getAstro(int month, int day) {

        int[] arr = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};// 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[month - 1]) {
            index = index - 1;
        }

        // 返回索引指向的星座string
        return astro[index];
    }

    /**
     * 根据年份获取属相
     *
     * @param year
     * @return
     */
    public static String getYear(Integer year) {
        if (year < 1900) {
            return "未知";
        }
        Integer start = 1900;

        return years[(year - start) % years.length];
    }

    /**
     * 获取天干地支
     *
     * @param num
     * @return
     */
    //============================== 传入 offset 传回干支, 0=甲子
    public static String cyclical(int num) {
        return (Gan[num % 10] + Zhi[num % 12]);
    }

}
