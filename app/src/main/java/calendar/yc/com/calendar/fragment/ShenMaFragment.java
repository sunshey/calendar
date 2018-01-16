package calendar.yc.com.calendar.fragment;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtools.RxImageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import calendar.yc.com.calendar.R;
import calendar.yc.com.calendar.adapter.ShenmaAdapter;
import calendar.yc.com.calendar.bean.CaipiaoNumberInfo;
import calendar.yc.com.calendar.bean.ShenmaInfo;

/**
 * Created by wanglin  on 2018/1/11 15:41.
 */

public class ShenMaFragment extends BaseFragment {
    @BindView(R.id.shenma_recyclerView)
    RecyclerView shenmaRecyclerView;

    private List<ShenmaInfo> list;
    private ShenmaAdapter shenmaAdapter;

    private String[] titles = {"双色球", "福彩3D", "七乐彩", "超级大乐透"};
    private String[] shuangseqiu_numbers = {"3", "4", "6", "1", "11", "5"};
    private String[] qilecai_numbers = {"5", "9", "1", "3", "5", "2", "7"};
    private String[] fucai_numbers = {"3", "6", "5"};
    private String[] daletou_numbers = {"5", "8", "9", "1", "2"};


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shenma;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        shenmaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shenmaAdapter = new ShenmaAdapter(list);
        shenmaRecyclerView.setAdapter(shenmaAdapter);
        shenmaRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                outRect.set(0, 0, 0, RxImageUtils.dip2px(getActivity(), 15));
            }
        });
        initData();
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            ShenmaInfo shenmaInfo = new ShenmaInfo();
            shenmaInfo.setTitle(titles[i]);
            shenmaInfo.setDate("01-11 周四");
            shenmaInfo.setQishu("18004");

            if (i == 0) {
                CaipiaoNumberInfo caipiaoNumberInfo = new CaipiaoNumberInfo();
                caipiaoNumberInfo.setRed(Arrays.asList(shuangseqiu_numbers));
                List<String> blue = new ArrayList<>();
                blue.add("7");
                caipiaoNumberInfo.setBlue(blue);
                shenmaInfo.setCaipiaoNumberInfo(caipiaoNumberInfo);
            } else if (i == 1) {
                CaipiaoNumberInfo caipiaoNumberInfo = new CaipiaoNumberInfo();
                caipiaoNumberInfo.setRed(Arrays.asList(fucai_numbers));
                shenmaInfo.setCaipiaoNumberInfo(caipiaoNumberInfo);

            } else if (i == 2) {
                CaipiaoNumberInfo caipiaoNumberInfo = new CaipiaoNumberInfo();
                caipiaoNumberInfo.setRed(Arrays.asList(qilecai_numbers));
                List<String> blue = new ArrayList<>();
                blue.add("8");
                caipiaoNumberInfo.setBlue(blue);
                shenmaInfo.setCaipiaoNumberInfo(caipiaoNumberInfo);

            } else if (i == 3) {
                CaipiaoNumberInfo caipiaoNumberInfo = new CaipiaoNumberInfo();
                caipiaoNumberInfo.setRed(Arrays.asList(daletou_numbers));
                List<String> blue = new ArrayList<>();
                blue.add("3");
                blue.add("6");
                caipiaoNumberInfo.setBlue(blue);
                shenmaInfo.setCaipiaoNumberInfo(caipiaoNumberInfo);
            }
            list.add(shenmaInfo);
        }
        shenmaAdapter.notifyDataSetChanged();

    }

}
