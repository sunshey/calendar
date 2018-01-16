package calendar.yc.com.calendar.fragment;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtools.RxImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import calendar.yc.com.calendar.R;
import calendar.yc.com.calendar.adapter.CaipiaoForcastAdapter;
import calendar.yc.com.calendar.bean.CaipiaoNewsInfo;
import calendar.yc.com.calendar.bean.CalendarNewsInfo;

/**
 * Created by wanglin  on 2018/1/12 19:41.
 */

public class CaipiaoForecastFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<CaipiaoNewsInfo> newsInfoList;
    private String[] titles = {"双色球", "大乐透", "福彩"};
    private CaipiaoForcastAdapter caipiaoForcastAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_caipiao_forecast;
    }

    @Override
    protected void init() {
        newsInfoList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        caipiaoForcastAdapter = new CaipiaoForcastAdapter(newsInfoList);
        recyclerView.setAdapter(caipiaoForcastAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, RxImageUtils.dip2px(getActivity(), 10));
            }
        });
        initData();
    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            CaipiaoNewsInfo caipiaoNewsInfo = new CaipiaoNewsInfo();
            caipiaoNewsInfo.setId(i);
            caipiaoNewsInfo.setTitle(titles[i]);
            caipiaoNewsInfo.setImgId(R.mipmap.every_day_sample);
            List<CalendarNewsInfo> calendarNewsInfos = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                calendarNewsInfos.add(new CalendarNewsInfo(R.mipmap.huangli_item_sample, "既招财，又旺妻的三大生肖男，你的他是吗？", 1965));
            }
            caipiaoNewsInfo.setiTemInfos(calendarNewsInfos);
            newsInfoList.add(caipiaoNewsInfo);
        }
        caipiaoForcastAdapter.notifyDataSetChanged();
    }


}
