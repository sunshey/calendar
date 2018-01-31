package yc.com.calendar.fragment;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.UIUitls;

import java.util.List;

import butterknife.BindView;
import yc.com.calendar.R;
import yc.com.calendar.adapter.CaipiaoForcastAdapter;
import yc.com.calendar.bean.CalendarNewsGroupInfo;
import yc.com.calendar.constants.SpConstant;
import yc.com.calendar.engine.NewsGroupEngine;
import yc.com.calendar.util.SimpleCacheUtils;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/12 19:41.
 */

public class CaipiaoForecastFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CaipiaoForcastAdapter caipiaoForcastAdapter;

    private NewsGroupEngine newsGroupEngine;
    private boolean cached;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_caipiao_forecast;
    }

    @Override
    protected void init() {
        newsGroupEngine = new NewsGroupEngine(getActivity());
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        caipiaoForcastAdapter = new CaipiaoForcastAdapter(null);
        recyclerView.setAdapter(caipiaoForcastAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, SizeUtils.dp2px(10));
            }
        });

    }

    @Override
    protected void getData() {

        SimpleCacheUtils.readCache(getActivity(), SpConstant.CAIPIAO_INFOS, new SimpleCacheUtils.CacheRunnable() {
            @Override
            public void run() {
                final List<CalendarNewsGroupInfo> calendarNewsGroupInfos = JSON.parseArray(this.getJson(), CalendarNewsGroupInfo.class);
                cached = true;
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        caipiaoForcastAdapter.setNewData(calendarNewsGroupInfos);

                    }
                });
            }
        });

        newsGroupEngine.getNewsGroupInfo().subscribe(new Action1<ResultInfo<List<CalendarNewsGroupInfo>>>() {
            @Override
            public void call(ResultInfo<List<CalendarNewsGroupInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    caipiaoForcastAdapter.setNewData(listResultInfo.data);
                    if (cached) {
                        SimpleCacheUtils.writeCache(getActivity(), SpConstant.CAIPIAO_INFOS, JSON.toJSONString(listResultInfo.data));
                    }
                }
            }
        });
    }

    @Override
    protected boolean isShowLoading() {
        return false;
    }


}
