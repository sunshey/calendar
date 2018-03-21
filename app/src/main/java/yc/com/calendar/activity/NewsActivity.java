package yc.com.calendar.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.blankj.utilcode.util.SizeUtils;
import yc.com.calendar.R;
import yc.com.calendar.adapter.NewsAdapter;
import yc.com.calendar.bean.CalendarNewsInfo;
import yc.com.calendar.callback.EmptyCallback;
import yc.com.calendar.callback.TimeoutCallback;
import yc.com.calendar.engine.NewsListEngine;
import yc.com.calendar.util.PostUtil;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/17 10:21.
 * 更多新闻
 */

public class NewsActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView_news_list)
    RecyclerView recyclerViewNewsList;
    private NewsAdapter newsAdapter;
    private List<CalendarNewsInfo> list;

    private NewsListEngine newsListEngine;
    private String typeId;
    private int PAGE = 1;
    private int LIMIT = 10;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_list;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        newsListEngine = new NewsListEngine(this);
        typeId = getIntent().getStringExtra("type_id");
        final String title = getIntent().getStringExtra("title");
        tvTitle.setText(title);


        RxView.clicks(llBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        recyclerViewNewsList.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(list);
        recyclerViewNewsList.setAdapter(newsAdapter);
        recyclerViewNewsList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, SizeUtils.dp2px(5), 0, 0);
            }
        });

        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CalendarNewsInfo calendarNewsInfo = (CalendarNewsInfo) adapter.getItem(position);
                Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("id", calendarNewsInfo.getId() + "");
                startActivity(intent);
            }
        });
        newsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                PAGE++;
                getData();
            }
        }, recyclerViewNewsList);
    }

    @Override
    protected void getData() {
        newsListEngine.getNewsInfoList(typeId, 0, PAGE, LIMIT).subscribe(new Action1<ResultInfo<List<CalendarNewsInfo>>>() {
            @Override
            public void call(ResultInfo<List<CalendarNewsInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK) {

                    if (listResultInfo.data != null && listResultInfo.data.size() > 0) {
                        if (PAGE == 1) {
                            newsAdapter.setNewData(listResultInfo.data);
                        } else {
                            newsAdapter.addData(listResultInfo.data);
                        }
                        newsAdapter.loadMoreComplete();
                        loadService.showSuccess();
                    } else {
                        newsAdapter.loadMoreEnd();
                        if (PAGE == 1)
                            PostUtil.postCallbackDelayed(loadService, EmptyCallback.class);
                    }
                } else {
                    if (PAGE == 1)
                        PostUtil.postCallbackDelayed(loadService, TimeoutCallback.class);
                }
            }
        });

    }

    @Override
    protected boolean isShowLoading() {
        return true;
    }


}
