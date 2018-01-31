package yc.com.calendar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.calendar.R;
import yc.com.calendar.adapter.CaipiaoListAdapter;
import yc.com.calendar.bean.CaipiaoInfo;
import yc.com.calendar.callback.EmptyCallback;
import yc.com.calendar.callback.TimeoutCallback;
import yc.com.calendar.engine.CaipiaoListEngine;
import yc.com.calendar.util.PostUtil;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/23 15:32.
 */

public class CaipiaoListActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.recyclerView_caipiao_list)
    RecyclerView recyclerViewCaipiaoList;
    private CaipiaoListEngine caipiaoListEngine;
    private String ticketId;
    private int PAGE = 1;
    private int PAGE_LIMIT = 10;
    private CaipiaoListAdapter caipiaoListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_caipiao_list;
    }

    @Override
    protected void init() {
        caipiaoListEngine = new CaipiaoListEngine(this);
        ticketId = getIntent().getStringExtra("ticket_id");
        String title = getIntent().getStringExtra("title");
        tvTitle.setText(title);

        RxView.clicks(llBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        recyclerViewCaipiaoList.setLayoutManager(new LinearLayoutManager(this));
        caipiaoListAdapter = new CaipiaoListAdapter(null);
        recyclerViewCaipiaoList.setAdapter(caipiaoListAdapter);

        initListener();
    }

    private void initListener() {
        caipiaoListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                PAGE++;
                getData();
            }
        }, recyclerViewCaipiaoList);
    }

    @Override
    protected void getData() {
        caipiaoListEngine.getCaipiaoList(ticketId, "0", PAGE, PAGE_LIMIT).subscribe(new Action1<ResultInfo<List<CaipiaoInfo>>>() {
            @Override
            public void call(ResultInfo<List<CaipiaoInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK) {
                    if (listResultInfo.data != null && listResultInfo.data.size() > 0) {
                        if (PAGE == 1) {
                            caipiaoListAdapter.setNewData(listResultInfo.data);
                        } else {
                            caipiaoListAdapter.addData(listResultInfo.data);
                        }
                        caipiaoListAdapter.loadMoreComplete();
                        loadService.showSuccess();
                    } else {
                        caipiaoListAdapter.loadMoreEnd();
                        if (PAGE == 1) {
                            PostUtil.postCallbackDelayed(loadService, EmptyCallback.class);
                        }
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
