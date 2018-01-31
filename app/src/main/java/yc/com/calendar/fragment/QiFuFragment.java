package yc.com.calendar.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import yc.com.calendar.R;
import yc.com.calendar.adapter.QifuAdapter;
import yc.com.calendar.bean.QifuInfo;

/**
 * Created by wanglin  on 2018/1/11 15:41.
 */

public class QiFuFragment extends BaseFragment {
    @BindView(R.id.recyclerView_qifu)
    RecyclerView recyclerViewQifu;
    @BindView(R.id.lampBuy)
    RadioButton lampBuy;
    @BindView(R.id.lampCenter)
    RadioButton lampCenter;
    @BindView(R.id.lampButtonGroup)
    RadioGroup lampButtonGroup;

    private List<QifuInfo> list;
    private QifuAdapter qifuAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qifu;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        recyclerViewQifu.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        qifuAdapter = new QifuAdapter(list);

        recyclerViewQifu.setAdapter(qifuAdapter);

        initData();
    }

    @Override
    protected void getData() {

    }

    @Override
    protected boolean isShowLoading() {
        return false;
    }

    private void initData() {

        list.add(new QifuInfo(R.mipmap.qifu_lamp_caiyun_small_1, "招财灯"));
        list.add(new QifuInfo(R.mipmap.qifu_lamp_shiye_small_2, "事业灯"));
        list.add(new QifuInfo(R.mipmap.qifu_lamp_pingan_small_3, "平安灯"));
        qifuAdapter.notifyDataSetChanged();


    }

}
