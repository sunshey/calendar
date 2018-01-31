package yc.com.calendar.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.UIUitls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.calendar.R;
import yc.com.calendar.activity.NewsActivity;
import yc.com.calendar.activity.NewsDetailActivity;
import yc.com.calendar.adapter.HuangLiAdapter;
import yc.com.calendar.bean.CalendarNewsInfo;
import yc.com.calendar.bean.HuangLiDbInfo;
import yc.com.calendar.constants.BusAction;
import yc.com.calendar.constants.SpConstant;
import yc.com.calendar.engine.NewsListEngine;
import yc.com.calendar.util.DatePickUtils;
import yc.com.calendar.util.DateUtils;
import yc.com.calendar.util.DbManager;
import yc.com.calendar.util.LunCalendarUtils;
import yc.com.calendar.util.SimpleCacheUtils;
import yc.com.calendar.util.WeekUtil;
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
    @BindView(R.id.fl_huangli_news)
    FrameLayout flHuangliNews;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tv_cover)
    TextView tvCover;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.ll_change_another)
    LinearLayout llChangeAnother;


    private List<CalendarNewsInfo> list;
    private HuangLiAdapter huangLiAdapter;
    private String currentMonth;
    private String currentYear;
    private String currentDay;
    private NewsListEngine newsListEngine;
    private CalendarNewsInfo calendarNewsInfo;

    private int PAGE = 1;
    private Animation animation;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_huangli;
    }

    @Override
    protected void init() {
        newsListEngine = new NewsListEngine(getActivity());

        currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        currentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        currentDay = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        huangLiAdapter = new HuangLiAdapter(null, true);
        recyclerView.setAdapter(huangLiAdapter);

        initListener();
    }

    private void initListener() {
        RxView.clicks(llNext).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String currentDate = getAfterDay(Integer.parseInt(currentYear), Integer.parseInt(currentMonth), Integer.parseInt(currentDay));
                String[] str = currentDate.split("-");
                setSelectedDate(str[0], str[1], str[2]);


                RxBus.get().post(BusAction.SHOW_LAST_NEXT_DATE, currentDate);
            }
        });
        RxView.clicks(llLast).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String currentDate = getBeforeDay(Integer.parseInt(currentYear), Integer.parseInt(currentMonth), Integer.parseInt(currentDay));

                String[] str = currentDate.split("-");
                setSelectedDate(str[0], str[1], str[2]);

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

        RxView.clicks(flHuangliNews).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("title", getString(R.string.huangli_infomation));
                intent.putExtra("id", calendarNewsInfo != null ? calendarNewsInfo.getId() + "" : "");
                startActivity(intent);
            }
        });

        huangLiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                calendarNewsInfo = (CalendarNewsInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("title", getString(R.string.huangli_infomation));
                intent.putExtra("id", calendarNewsInfo != null ? calendarNewsInfo.getId() + "" : "");
                startActivity(intent);
            }
        });

        RxView.clicks(rlMore).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("title", getString(R.string.huangli_infomation));
                intent.putExtra("type_id", "2");
                startActivity(intent);
            }
        });

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.refresh_rotate);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        RxView.clicks(llChangeAnother).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ivRefresh.startAnimation(animation);//开始动画
                PAGE++;
                getData();

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

    @Override
    protected void getData() {

        SimpleCacheUtils.readCache(getActivity(), SpConstant.HUANGLI_INFO, new SimpleCacheUtils.CacheRunnable() {
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

        newsListEngine.getNewsInfoList("2", 0, PAGE, 4).subscribe(new Action1<ResultInfo<List<CalendarNewsInfo>>>() {
            @Override
            public void call(ResultInfo<List<CalendarNewsInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {

                    List<CalendarNewsInfo> calendarNewsInfos = listResultInfo.data;
                    setData(calendarNewsInfos);
                    SimpleCacheUtils.writeCache(getActivity(), SpConstant.HUANGLI_INFO, JSON.toJSONString(calendarNewsInfos));
                }

                if (animation != null) {
                    animation.cancel();
                }
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append(currentYear).append("-").append(currentMonth).append("-").append(currentDay);
        setCommonDate(sb.toString());
    }

    private void setData(List<CalendarNewsInfo> calendarNewsInfos) {
        if (calendarNewsInfos.size() > 0) {
            calendarNewsInfo = calendarNewsInfos.get(0);
            Glide.with(getActivity()).load(calendarNewsInfo.getImg()).apply(new RequestOptions().error(R.mipmap.every_day_sample)).into(ivCover);
            tvCover.setText(calendarNewsInfo.getTitle());
            list = calendarNewsInfos.subList(1, calendarNewsInfos.size());
            huangLiAdapter.setNewData(list);
        }
    }

    @Override
    protected boolean isShowLoading() {
        return false;
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


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_YEAR_MONTH)
            }
    )
    public void getYearMonthDate(String date) {
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
