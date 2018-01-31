package yc.com.calendar.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.UIUitls;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.calendar.R;
import yc.com.calendar.activity.NewsDetailActivity;
import yc.com.calendar.bean.CalendarNewsInfo;
import yc.com.calendar.bean.HuangLiDbInfo;
import yc.com.calendar.bean.PayWayInfo;
import yc.com.calendar.constants.BusAction;
import yc.com.calendar.constants.SpConstant;
import yc.com.calendar.engine.NewsListEngine;
import yc.com.calendar.engine.PaywayInfoEngine;
import yc.com.calendar.util.DateUtils;
import yc.com.calendar.util.DbManager;
import yc.com.calendar.util.PaywayInfoHelper;
import yc.com.calendar.util.SimpleCacheUtils;
import yc.com.calendar.util.WeekUtil;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import rx.functions.Action1;

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
    @BindView(R.id.fl_every_word)
    FrameLayout flEveryWord;
    @BindView(R.id.iv_every_day)
    ImageView ivEveryDay;
    @BindView(R.id.tv_every_content)
    TextView tvEveryContent;

    private int currentMoth;
    private int currentYear;
    private int currentDay;

    private NewsListEngine newsListEngine;
    private CalendarNewsInfo newsInfo;
    private PaywayInfoEngine paywayInfoEngine;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void init() {
        newsListEngine = new NewsListEngine(getActivity());
        paywayInfoEngine = new PaywayInfoEngine(getActivity());

        initDatePicker();
    }

    @Override
    protected void getData() {
        StringBuilder sb = new StringBuilder();
        sb.append(Calendar.getInstance().get(Calendar.YEAR)).append("-").append(Calendar.getInstance().get(Calendar.MONTH) + 1).append("-").append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        setDate(sb.toString());
        SimpleCacheUtils.readCache(getActivity(), SpConstant.INDEX_INFO, new SimpleCacheUtils.CacheRunnable() {
            @Override
            public void run() {
                final List<CalendarNewsInfo> calendarNewsInfos = JSON.parseArray(this.getJson(), CalendarNewsInfo.class);
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        setData(calendarNewsInfos);
                    }
                });
            }
        });


        newsListEngine.getNewsInfoList("1", 0, 1, 1).subscribe(new Action1<ResultInfo<List<CalendarNewsInfo>>>() {


            @Override
            public void call(ResultInfo<List<CalendarNewsInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    List<CalendarNewsInfo> calendarNewsInfos = listResultInfo.data;
                    setData(calendarNewsInfos);
                    SimpleCacheUtils.writeCache(getActivity(), SpConstant.INDEX_INFO, JSON.toJSONString(calendarNewsInfos));
                }
            }
        });

        initListener();

        paywayInfoEngine.getPayWayInfoList().subscribe(new Action1<ResultInfo<List<PayWayInfo>>>() {
            @Override
            public void call(ResultInfo<List<PayWayInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    List<PayWayInfo> payWayInfos = listResultInfo.data;
                    PaywayInfoHelper.setPayWayInfoList(payWayInfos);
                }

            }
        });
    }

    private void setData(List<CalendarNewsInfo> calendarNewsInfos) {
        if (calendarNewsInfos.size() > 0) {
            newsInfo = calendarNewsInfos.get(0);
            Glide.with(getActivity()).load(newsInfo.getImg()).apply(new RequestOptions().error(R.mipmap.every_day_sample)).into(ivEveryDay);
            tvEveryContent.setText(newsInfo.getTitle());
        }
    }

    @Override
    protected boolean isShowLoading() {
        return false;
    }


    private void initListener() {
        RxView.clicks(flEveryWord).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("title", getString(R.string.every_day_text));
                intent.putExtra("id", newsInfo != null ? newsInfo.getId() + "" : "");
                startActivity(intent);
            }
        });
    }


    private void initDatePicker() {
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMoth = Calendar.getInstance().get(Calendar.MONTH) + 1;

        datePicker.setDate(currentYear, currentMoth);
        datePicker.setMode(DPMode.SINGLE);
        datePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                if (!TextUtils.isEmpty(date)) {
                    currentDay = Integer.parseInt(date.split("-")[2]);
                    RxBus.get().post(BusAction.SHOW_CURRENT_DATE, date);
                    setDate(date);
                }
            }
        });


        datePicker.setOnMonthChangedListener(new DatePicker.onMonthChangedListener() {
            @Override
            public void onMonthChanged(int month) {

                currentMoth = month;
                RxBus.get().post(BusAction.SHOW_YEAR_MONTH, currentYear + "-" + currentMoth + "-" + currentDay);
                setDate(currentYear + "-" + currentMoth + "-" + currentDay);

            }
        });
        datePicker.setOnYearChangedListener(new DatePicker.onYearChangedListener() {
            @Override
            public void onYearChanged(int year) {

                currentYear = year;
                RxBus.get().post(BusAction.SHOW_YEAR_MONTH, currentYear + "-" + currentMoth + "-" + currentDay);
                setDate(currentYear + "-" + currentMoth + "-" + currentDay);
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

        String[] split = date.split("-");
        if (split[1].startsWith("0")) {
            split[1].replace("0", "");
        }

        datePicker.setDate1(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

    }

}
