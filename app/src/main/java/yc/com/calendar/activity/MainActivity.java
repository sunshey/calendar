package yc.com.calendar.activity;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxSPTool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.calendar.R;
import yc.com.calendar.adapter.MainAdapter;
import yc.com.calendar.constants.BusAction;
import yc.com.calendar.constants.SpConstant;
import yc.com.calendar.fragment.CaipiaoForecastFragment;
import yc.com.calendar.fragment.HuangLiFragment;
import yc.com.calendar.fragment.IndexFragment;
import yc.com.calendar.fragment.ShenMaFragment;
import yc.com.calendar.util.DatePickUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener, EasyPermissions.PermissionCallbacks {


    @BindView(R.id.main_viewPager)
    ViewPager mMainViewPager;
    @BindView(R.id.main_bottom_bar)
    BottomNavigationBar mainBottomBar;
    @BindView(R.id.tv_mainDate)
    TextView tvMainDate;
    @BindView(R.id.main_share)
    ImageView mainShare;
    @BindView(R.id.main_top)
    RelativeLayout mainTop;
    @BindView(R.id.iv_today)
    ImageView ivToday;
    @BindView(R.id.ll_down_arrow)
    LinearLayout ivDownArrow;
    @BindView(R.id.ll_date)
    LinearLayout llDate;

    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;

    private List<Fragment> mList; //ViewPager的数据源
    private String currentYear;
    private String currentMonth;

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
        methodRequiresTwoPermission();
    }

    @Override
    protected void getData() {

    }

    @Override
    protected boolean isShowLoading() {
        return false;
    }

    private void initListener() {
        RxView.clicks(ivToday).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setCurrentDate(currentYear, currentMonth);
                RxBus.get().post(BusAction.SHOW_TODAY_DATE, "show today");
            }
        });

        RxView.clicks(ivDownArrow).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                DatePickUtils.showDatePicker(MainActivity.this, new DatePickUtils.onDatePickListener() {
                    @Override
                    public void onDatePick(String year, String month, String day) {
                        setCurrentDate(year, month);
                        RxBus.get().post(BusAction.SHOW_SELECTED_DATE, year + "-" + month + "-" + day);
                    }
                });
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

        mainBottomBar.selectTab(position);
        if (position == 0) {
            llDate.setVisibility(View.VISIBLE);
            tvMainTitle.setVisibility(View.GONE);
            String str = RxSPTool.getString(this, SpConstant.CURRENT_DATE);
            if (!TextUtils.isEmpty(str)) {
                tvMainDate.setText(str);
            }

        } else {
            llDate.setVisibility(View.GONE);
            tvMainTitle.setVisibility(View.VISIBLE);
        }
        if (position == 1) {
            tvMainTitle.setText(getString(R.string.main_huangli));
        } else if (position == 2) {
            tvMainTitle.setText(getString(R.string.main_caipiao));
            ivToday.setVisibility(View.GONE);
        } else if (position == 3) {
            tvMainTitle.setText(getString(R.string.main_shenma));
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
        String[] clickDate = date.split("-");
        setCurrentDate(clickDate[0], clickDate[1]);


    }

    private void setCurrentDate(String year, String month) {
        String currentDate = String.format(getString(R.string.current_date), year, month);
        tvMainDate.setText(currentDate);
        RxSPTool.putString(this, SpConstant.CURRENT_DATE, currentDate);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHOW_YEAR_MONTH)
            }
    )
    public void getCurrentYearMonth(String date) {
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
        String[] clickDate = date.split("-");
        setCurrentDate(clickDate[0], clickDate[1]);

    }


    public static final int STORGE = 100;

    @AfterPermissionGranted(STORGE)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.storge), STORGE, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // 允许权限
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // 权限拒绝
        new AppSettingsDialog.Builder(this).build().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private long time = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time > 2000) {
            Toast.makeText(this, "再按一次退出" + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            time = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
