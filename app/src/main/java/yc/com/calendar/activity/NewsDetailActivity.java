package yc.com.calendar.activity;

import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.calendar.R;
import yc.com.calendar.bean.CalendarNewsInfo;
import yc.com.calendar.callback.EmptyCallback;
import yc.com.calendar.callback.TimeoutCallback;
import yc.com.calendar.engine.NewsDetailEngine;
import yc.com.calendar.util.PostUtil;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/16 19:23.
 */

public class NewsDetailActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_share)
    ImageView tvShare;
    @BindView(R.id.mTextViewTitle)
    TextView mTextViewTitle;
    @BindView(R.id.mTextViewFrom)
    TextView mTextViewFrom;
    @BindView(R.id.mTextViewTime)
    TextView mTextViewTime;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.ll_rootView)
    LinearLayout llRootView;
    private NewsDetailEngine newsDetailEngine;

    private String news_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void init() {
        newsDetailEngine = new NewsDetailEngine(this);
        RxView.clicks(tvBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        String title = getIntent().getStringExtra("title");
        tvTitle.setText(title);
        news_id = getIntent().getStringExtra("id");


    }

    @Override
    protected void getData() {
        newsDetailEngine.getNewsInfoDetail(news_id).subscribe(new Action1<ResultInfo<CalendarNewsInfo>>() {
            @Override
            public void call(ResultInfo<CalendarNewsInfo> calendarNewsInfoResultInfo) {
                if (calendarNewsInfoResultInfo != null && calendarNewsInfoResultInfo.code == HttpConfig.STATUS_OK) {
                    if (calendarNewsInfoResultInfo.data != null) {
                        loadService.showSuccess();
                        CalendarNewsInfo calendarNewsInfo = calendarNewsInfoResultInfo.data;
                        mTextViewTitle.setText(calendarNewsInfo.getTitle());
                        String str = getString(R.string.from_author);
                        mTextViewFrom.setText(String.format(str,
                                TextUtils.isEmpty(calendarNewsInfo.getAuthor()) ? getString(R.string.app_name) : calendarNewsInfo.getAuthor()));

                        String add_time = calendarNewsInfo.getAdd_time();
                        if (!TextUtils.isEmpty(add_time)) {
                            add_time = TimeUtils.millis2String(Long.parseLong(add_time) * 1000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
                            mTextViewTime.setText(add_time);
                        }

                        initWebView(calendarNewsInfo.getBody());
                    } else {
                        PostUtil.postCallbackDelayed(loadService, EmptyCallback.class);
                    }
                } else {
                    PostUtil.postCallbackDelayed(loadService, TimeoutCallback.class);
                }
            }
        });
    }

    @Override
    protected boolean isShowLoading() {
        return true;
    }

    private void initWebView(String body) {
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//        webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(false);//设置是否加载网络图片 true 为不加载 false 为加载
        webSettings.setSupportZoom(true);
        webView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);


    }

}
