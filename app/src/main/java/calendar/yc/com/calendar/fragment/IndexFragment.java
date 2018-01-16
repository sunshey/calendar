package calendar.yc.com.calendar.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.vondear.rxtools.RxLogUtils;
import com.vondear.rxtools.RxSPUtils;
import com.vondear.rxtools.RxUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import calendar.yc.com.calendar.R;
import calendar.yc.com.calendar.bean.HuangLiDbInfo;
import calendar.yc.com.calendar.constants.BusAction;
import calendar.yc.com.calendar.constants.SpConstant;
import calendar.yc.com.calendar.util.DateUtils;
import calendar.yc.com.calendar.util.DbManager;
import calendar.yc.com.calendar.util.WeekUtil;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by wanglin  on 2018/1/11 15:41.
 */

public class IndexFragment extends BaseFragment {

    @BindView(R.id.date_picker)
    DatePicker datePicker;
    @BindView(R.id.tv_today)
    TextView tvToday;
    @BindView(R.id.tv_lunar)
    TextView tvLunar;
    @BindView(R.id.tv_weekday)
    TextView tvWeekday;
    @BindView(R.id.tv_yi)
    TextView tvYi;
    @BindView(R.id.tv_ji)
    TextView tvJi;
    @BindView(R.id.tv_sort_week)
    TextView tvSortWeek;
    @BindView(R.id.tv_constellation)
    TextView tvConstellation;
    @BindView(R.id.tv_tiangan_zodiac)
    TextView tvTianganZodiac;


    private int currentMoth;
    private int currentYear;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void init() {
        initDatePicker();
        initData();
    }

    private void initData() {


        StringBuilder sb = new StringBuilder();
        sb.append(Calendar.getInstance().get(Calendar.YEAR)).append("-").append(Calendar.getInstance().get(Calendar.MONTH) + 1).append("-").append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        setDate(sb.toString());

    }


    private void initDatePicker() {

        datePicker.setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1);
        datePicker.setMode(DPMode.SINGLE);
        datePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                RxLogUtils.e("TAG", date);
                if (!TextUtils.isEmpty(date)) {
                    RxBus.get().post(BusAction.SHOW_CURRENT_DATE, date);
                    setDate(date);
                }
            }
        });

        datePicker.setOnMonthChangedListener(new DatePicker.onMonthChangedListener() {
            @Override
            public void onMonthChanged(int month) {
                RxLogUtils.e("TAG", month);
                currentMoth = month;
                RxBus.get().post(BusAction.SHOW_YEAR_MONTH, currentYear + "-" + currentMoth);

            }
        });
        datePicker.setOnYearChangedListener(new DatePicker.onYearChangedListener() {
            @Override
            public void onYearChanged(int year) {
                RxLogUtils.e("TAG", year);
                currentYear = year;
                RxBus.get().post(BusAction.SHOW_YEAR_MONTH, currentYear + "-" + currentMoth);
            }
        });

    }


    private void setDate(String currentDate) {

        String[] split = currentDate.split("-");

        tvToday.setText(split[2]);
        tvWeekday.setText(WeekUtil.assignDate2Week(currentDate));
        String date = DateUtils.getCurrentLunar(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        tvLunar.setText(date);
        HuangLiDbInfo yiJiInfo = DbManager.getManager().getYiJiInfo(currentDate);
        if (yiJiInfo != null) {
            tvYi.setText(yiJiInfo.getYi());
            tvJi.setText(yiJiInfo.getJi());
        }
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(split[0]), Integer.parseInt(split[1]) - 1, Integer.parseInt(split[2]));

        tvSortWeek.setText(String.format(getString(R.string.sort_week), String.valueOf(c.get(Calendar.WEEK_OF_MONTH))));
        tvConstellation.setText(DateUtils.getAstro(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        tvTianganZodiac.setText(String.format(getString(R.string.tiangan_zodiac), DateUtils.cyclical(Integer.parseInt(split[2])), DateUtils.getYear(Integer.parseInt(split[0]))));


    }

    private void scrollMonthView(int month) {

        String saveDate = RxSPUtils.getString(getActivity(), SpConstant.CURRENT_MONTH);
        if (!TextUtils.isEmpty(saveDate)) {
            RxLogUtils.e("TAG", "setDate:" + saveDate + "--" + month + "---" + (month - Integer.parseInt(saveDate)));
            datePicker.getMonthView().showNectMonth(month - Integer.parseInt(saveDate));

        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_TODAY_DATE)
            }
    )
    public void getCurrentDate(String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(Calendar.getInstance().get(Calendar.YEAR)).append("-").append(Calendar.getInstance().get(Calendar.MONTH) + 1).append("-").append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        setDate(sb.toString());
        datePicker.setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_LAST_NEXT_DATE)
            }
    )
    public void getLastOrNextDate(String date) {
        setDate(date);

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_SELECTED_DATE)
            }
    )
    public void getSelectedDate(String date) {
        setDate(date);
//        scrollMonthView(date);
        String[] split = date.split("-");
        scrollMonthView(Integer.parseInt(split[1]));
//        datePicker.setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1);
    }


}
