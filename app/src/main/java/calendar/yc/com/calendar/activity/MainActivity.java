package calendar.yc.com.calendar.activity;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.util.SPUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxLogUtils;
import com.vondear.rxtools.RxSPUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import calendar.yc.com.calendar.R;
import calendar.yc.com.calendar.adapter.MainAdapter;
import calendar.yc.com.calendar.constants.BusAction;
import calendar.yc.com.calendar.constants.SpConstant;
import calendar.yc.com.calendar.fragment.CaipiaoForecastFragment;
import calendar.yc.com.calendar.fragment.HuangLiFragment;
import calendar.yc.com.calendar.fragment.IndexFragment;
import calendar.yc.com.calendar.fragment.ShenMaFragment;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.main_viewPager)
    ViewPager mMainViewPager;
    @BindView(R.id.main_bottom_bar)
    BottomNavigationBar mainBottomBar;
    @BindView(R.id.tv_mainDate)
    TextView tvMainDate;
    @BindView(R.id.ll_main_date)
    LinearLayout llMainDate;
    @BindView(R.id.main_share)
    ImageView mainShare;
    @BindView(R.id.main_top)
    RelativeLayout mainTop;
    @BindView(R.id.iv_today)
    ImageView ivToday;
    @BindView(R.id.iv_down_arrow)
    ImageView ivDownArrow;

    private List<Fragment> mList; //ViewPager的数据源
    private String currentYear;
    private String currentMonth;
    private int currentPos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

        mList = new ArrayList<>();
        mList.add(new IndexFragment());
        mList.add(new HuangLiFragment());
        mList.add(new CaipiaoForecastFragment());
        mList.add(new ShenMaFragment());
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), mList);
        mMainViewPager.setAdapter(mainAdapter); //视图加载适配器
        mMainViewPager.setOffscreenPageLimit(4);
        mMainViewPager.addOnPageChangeListener(this);
        mainBottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        mainBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mainBottomBar.setBarBackgroundColor(R.color.gray_fefefe);

        mainBottomBar.addItem((new BottomNavigationItem(R.mipmap.main_index_press, getString(R.string.main_index)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_index_normal))).setInActiveColorResource(R.color.gray_999999).setActiveColorResource(R.color.red_d03f3f))
                .addItem((new BottomNavigationItem(R.mipmap.main_huangli_press, getString(R.string.main_huangli)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_huangli_normal))).setInActiveColorResource(R.color.gray_999999).setActiveColorResource(R.color.red_d03f3f))
                .addItem((new BottomNavigationItem(R.mipmap.main_qifu_press, getString(R.string.main_caipiao)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_qifu_normal))).setInActiveColorResource(R.color.gray_999999).setActiveColorResource(R.color.red_d03f3f))
                .addItem((new BottomNavigationItem(R.mipmap.main_shenma_press, getString(R.string.main_shenma)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_shenma_noraml))).setInActiveColorResource(R.color.gray_999999).setActiveColorResource(R.color.red_d03f3f))
                .setFirstSelectedPosition(0)
                .initialise();

        mainBottomBar.setTabSelectedListener(this);
        currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        currentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        setCurrentDate(currentYear, currentMonth);
        initListener();

    }

    private void initListener() {
        RxView.clicks(ivToday).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setCurrentDate(currentYear, currentMonth);

                RxBus.get().post(BusAction.SHOW_TODAY_DATE, "show today");
            }
        });
    }

    @Override
    public void onTabSelected(int position) {
        mMainViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RxLogUtils.e("TAG", "POSTIONT: " + position);
        currentPos = position;
        mainBottomBar.selectTab(position);
        if (position == 0) {
            ivDownArrow.setVisibility(View.VISIBLE);
            String str = RxSPUtils.getString(this, SpConstant.CURRENT_DATE);
            if (!TextUtils.isEmpty(str))
                tvMainDate.setText(str);
            ivToday.setVisibility(View.VISIBLE);
        } else {
            ivDownArrow.setVisibility(View.GONE);
        }
        if (position == 1) {
            tvMainDate.setText(getString(R.string.main_huangli));
            ivToday.setVisibility(View.VISIBLE);
        } else if (position == 2) {
            tvMainDate.setText(getString(R.string.main_caipiao));
            ivToday.setVisibility(View.GONE);
        } else if (position == 3) {
            tvMainDate.setText(getString(R.string.main_shenma));
            ivToday.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_CURRENT_DATE)
            }
    )
    public void getCurrentDate(String date) {
        RxLogUtils.e("TAG", "getCurrentDate:" + date);
        String[] clickDate = date.split("-");
        setCurrentDate(clickDate[0], clickDate[1]);


    }

    private void setCurrentDate(String year, String month) {
        String currentDate = String.format(getString(R.string.current_date), year, month);
        tvMainDate.setText(currentDate);
        RxSPUtils.putString(this, SpConstant.CURRENT_DATE, currentDate);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_YEAR_MONTH)
            }
    )
    public void getCurrentYearMonth(String date) {
        RxLogUtils.e("TAG", "getCurrentDate:" + date);
        String[] clickDate = date.split("-");
        setCurrentDate(clickDate[0], clickDate[1]);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_LAST_NEXT_DATE)
            }
    )
    public void getLastOrNextDate(String date) {
        RxLogUtils.e("TAG", "POS: " + currentPos);
        if (currentPos == 0) {
            String[] clickDate = date.split("-");
            setCurrentDate(clickDate[0], clickDate[1]);
        }
    }
}
