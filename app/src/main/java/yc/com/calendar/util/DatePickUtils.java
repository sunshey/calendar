package yc.com.calendar.util;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.Calendar;

import yc.com.calendar.R;
import cn.qqtheme.framework.picker.DatePicker;


/**
 * Created by wanglin  on 2018/1/16 13:44.
 */

public class DatePickUtils {

    public static void showDatePicker(Activity activity, final onDatePickListener listener) {

        final DatePicker picker = new DatePicker(activity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(SizeUtils.sp2px(10));
        picker.setRangeEnd(2111, 12, 31);
        picker.setRangeStart(2000, 1, 1);
//        picker.setTextColor(activity.getResources().getColor(R.color.red_d03f3f));
//        picker.setLabelTextColor(activity.getResources().getColor(R.color.red_d03f3f));
//        picker.setDividerColor(activity.getResources().getColor(R.color.red_d03f3f));
        picker.setCancelTextColor(activity.getResources().getColor(R.color.gray_cccccc));


        picker.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                LogUtils.e("TAG", year + "--" + month + "--" + day);
                listener.onDatePick(year, month, day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();


    }


    public interface onDatePickListener {
        void onDatePick(String year, String month, String day);
    }


}
