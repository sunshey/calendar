package calendar.yc.com.calendar.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxLogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import calendar.yc.com.calendar.R;
import calendar.yc.com.calendar.adapter.HuangLiAdapter;
import calendar.yc.com.calendar.bean.CalendarNewsInfo;
import calendar.yc.com.calendar.bean.HuangLiDbInfo;
import calendar.yc.com.calendar.constants.BusAction;
import calendar.yc.com.calendar.util.DatePickUtils;
import calendar.yc.com.calendar.util.DateUtils;
import calendar.yc.com.calendar.util.DbManager;
import calendar.yc.com.calendar.util.LunCalendarUtils;
import calendar.yc.com.calendar.util.WeekUtil;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/11 15:41.
 */

public class HuangLiFragment extends BaseFragment {
    @BindView(R.id.tv_huangli_date)
    TextView tvHuangliDate;
    @BindView(R.id.tv_yi)
    TextView tvYi;
    @BindView(R.id.tv_ji)
    TextView tvJi;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.iv_first)
    ImageView ivFirst;
    @BindView(R.id.iv_second)
    ImageView ivSecond;
    @BindView(R.id.iv_three)
    ImageView ivThree;
    @BindView(R.id.iv_four)
    ImageView ivFour;
    @BindView(R.id.ll_last)
    LinearLayout llLast;
    @BindView(R.id.ll_next)
    LinearLayout llNext;
    @BindView(R.id.tv_tiangan_zodiac)
    TextView tvTianganZodiac;
    @BindView(R.id.tv_weekday)
    TextView tvWeekday;
    @BindView(R.id.tv_sort_week)
    TextView tvSortWeek;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_select_date)
    RelativeLayout rlSelectDate;


    private List<CalendarNewsInfo> list;
    private HuangLiAdapter huangLiAdapter;
    private String currentMonth;
    private String currentYear;
    private String currentDay;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_huangli;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        currentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        currentDay = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        huangLiAdapter = new HuangLiAdapter(list, true);
        recyclerView.setAdapter(huangLiAdapter);
        initData();
        initListener();
    }

    private void initListener() {
        RxView.clicks(llNext).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String currentDate = getAfterDay(Integer.parseInt(currentYear), Integer.parseInt(currentMonth), Integer.parseInt(currentDay));
                String[] str = currentDate.split("-");
                setSelectedDate(str[0],str[1],str[2]);


                RxBus.get().post(BusAction.SHOW_LAST_NEXT_DATE, currentDate);
            }
        });
        RxView.clicks(llLast).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String currentDate = getBeforeDay(Integer.parseInt(currentYear), Integer.parseInt(currentMonth), Integer.parseInt(currentDay));

                String[] str = currentDate.split("-");
                setSelectedDate(str[0],str[1],str[2]);

                RxBus.get().post(BusAction.SHOW_LAST_NEXT_DATE, currentDate);
            }
        });

        RxView.clicks(rlSelectDate).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                DatePickUtils.showDatePicker(getActivity(), new DatePickUtils.onDatePickListener() {
                    @Override
                    public void onDatePick(String year, String month, String day) {
                        setSelectedDate(year, month, day);

                        RxBus.get().post(BusAction.SHOW_SELECTED_DATE, year + "-" + month + "-" + day);
                    }
                });
            }
        });
    }

    private void setSelectedDate(String year, String month, String day) {
        currentYear = year;
        currentMonth = month;
        currentDay = day;
        if (currentMonth.startsWith("0")) {
            currentMonth = currentMonth.replace("0", "");
        }
        if (currentDay.startsWith("0")) {
            currentDay = currentDay.replace("0", "");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(currentYear).append("-").append(currentMonth).append("-").append(currentDay);
        setCommonDate(sb.toString());
    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            list.add(new CalendarNewsInfo(R.mipmap.huangli_item_sample, "既招财，又旺妻的三大生肖男，你的他是吗？", 1965));
        }
        huangLiAdapter.notifyDataSetChanged();
        StringBuilder sb = new StringBuilder();
        sb.append(currentYear).append("-").append(currentMonth).append("-").append(currentDay);
        setCommonDate(sb.toString());
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_CURRENT_DATE)
            }
    )
    public void getCurrentDate(String date) {
        setCommonDate(date);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_SELECTED_DATE)
            }
    )
    public void getSelectDate(String date) {
        setCommonDate(date);
    }

    private void setYiJiInfo(String date) {
        HuangLiDbInfo yiJiInfo = DbManager.getManager().getYiJiInfo(date);
        if (yiJiInfo != null) {
            tvYi.setText(yiJiInfo.getYi());
            tvJi.setText(yiJiInfo.getJi());
        }
    }


    private void setLunCalendar() {
        String date = DateUtils.getCurrentLunar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth), Integer.parseInt(currentDay));
        RxLogUtils.e("TAG", "setLunCalendar:  " + date);
        StringBuilder stringBuilder = new StringBuilder(date);
        LunCalendarUtils.setLunCalendarImage1(String.valueOf(stringBuilder.charAt(0)), ivFirst);
        LunCalendarUtils.setLunCalendarImage2(String.valueOf(stringBuilder.charAt(2)), ivThree);
        LunCalendarUtils.setLunCalendarImage3(String.valueOf(stringBuilder.charAt(3)), ivFour);


    }


    /**
     * 获取当前时间的前一天时间
     *
     * @param
     * @return
     */
    private String getBeforeDay(int year, int month, int day) {
        //使用roll方法进行向前回滚
        //cl.roll(Calendar.DATE, -1);
        //使用set方法直接进行设置

        Calendar cl = Calendar.getInstance();
        cl.set(year, month - 1, day - 1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        RxLogUtils.e("TAG", sf.format(cl.getTime()));
        return sf.format(cl.getTime());
    }

    /**
     * 获取当前时间的后一天时间
     *
     * @param
     * @return
     */
    private String getAfterDay(int year, int month, int day) {
        //使用roll方法进行回滚到后一天的时间
        //cl.roll(Calendar.DATE, 1);
        //使用set方法直接设置时间值
        Calendar cl = Calendar.getInstance();
        cl.set(year, month - 1, day + 1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        RxLogUtils.e("TAG", sf.format(cl.getTime()));
        return sf.format(cl.getTime());
    }


    private void setCommonDate(String currentDate) {
        String[] clickDate = currentDate.split("-");
        currentYear = clickDate[0];
        currentMonth = clickDate[1];
        currentDay = clickDate[2];
        tvHuangliDate.setText(String.format(getString(R.string.current_date_day), currentYear, currentMonth, currentDay));
        setLunCalendar();
        setYiJiInfo(currentDate);
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(currentYear), Integer.parseInt(currentMonth) - 1, Integer.parseInt(currentDay));
        tvSortWeek.setText(String.format(getString(R.string.sort_week), String.valueOf(c.get(Calendar.WEEK_OF_MONTH))));
        tvWeekday.setText(WeekUtil.assignDate2Week(currentDate));
        tvTianganZodiac.setText(String.format(getString(R.string.tiangan_zodiac), DateUtils.cyclical(Integer.parseInt(currentDay)), DateUtils.getYear(Integer.parseInt(currentYear))));
    }


}
