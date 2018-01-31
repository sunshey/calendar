package yc.com.calendar.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.pay.OrderInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.UIUitls;

import java.util.List;

import butterknife.BindView;
import yc.com.calendar.R;
import yc.com.calendar.activity.CaipiaoListActivity;
import yc.com.calendar.adapter.ShenmaAdapter;
import yc.com.calendar.bean.CaipiaoInfo;
import yc.com.calendar.bean.CaipiaoInfoWrapper;
import yc.com.calendar.bean.VipInfo;
import yc.com.calendar.constants.BusAction;
import yc.com.calendar.constants.SpConstant;
import yc.com.calendar.engine.CaipiaoEngine;
import yc.com.calendar.engine.VipInfoEngine;
import yc.com.calendar.util.SimpleCacheUtils;
import yc.com.calendar.util.VipInfoHelper;
import yc.com.calendar.widget.PayDialog;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/11 15:41.
 */

public class ShenMaFragment extends BaseFragment {
    @BindView(R.id.shenma_recyclerView)
    RecyclerView shenmaRecyclerView;


    private ShenmaAdapter shenmaAdapter;

    private int timeNum = 0;


    private CaipiaoEngine caipiaoEngine;

    private VipInfoEngine vipInfoEngine;
    private boolean cached;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shenma;
    }

    @Override
    protected void init() {

        caipiaoEngine = new CaipiaoEngine(getActivity());
        vipInfoEngine = new VipInfoEngine(getActivity());

        shenmaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shenmaAdapter = new ShenmaAdapter(null);
        shenmaRecyclerView.setAdapter(shenmaAdapter);
        TextView textView = new TextView(getActivity());
        textView.setText("免责声明：本软件预测结果仅供参考，请理智选彩。");
        textView.getPaint().setTextSize(SizeUtils.sp2px(12));
        textView.setGravity(Gravity.CENTER);
        shenmaAdapter.addFooterView(textView);
        shenmaRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                outRect.set(0, 0, 0, SizeUtils.dp2px(15));
            }
        });

        initListener();
    }

    @Override
    protected void getData() {
        SimpleCacheUtils.readCache(getActivity(), SpConstant.FORECAST_INFOS, new SimpleCacheUtils.CacheRunnable() {
            @Override
            public void run() {
                final CaipiaoInfoWrapper infoWrapper = JSON.parseObject(this.getJson(), CaipiaoInfoWrapper.class);
                cached = true;
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        setData(infoWrapper);
                    }
                });
            }
        });

        caipiaoEngine.getCaipiaoInfoList().subscribe(new Action1<ResultInfo<CaipiaoInfoWrapper>>() {
            @Override
            public void call(ResultInfo<CaipiaoInfoWrapper> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK) {
                    setData(listResultInfo.data);
                    if (cached) {

                        SimpleCacheUtils.writeCache(getActivity(), SpConstant.FORECAST_INFOS, JSON.toJSONString(listResultInfo.data));
                    }
                }
            }
        });

        vipInfoEngine.getVipInfoList().subscribe(new Action1<ResultInfo<List<VipInfo>>>() {
            @Override
            public void call(ResultInfo<List<VipInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    VipInfoHelper.setGoodInfoList(listResultInfo.data);
                }
            }
        });


    }

    private void setData(CaipiaoInfoWrapper infoWrapper) {
        if (infoWrapper != null) {
            shenmaAdapter.setmEffectTime(infoWrapper.getEffect_time());
            if (infoWrapper.getLists() != null)
                shenmaAdapter.setNewData(infoWrapper.getLists());

        }
    }

    @Override
    protected boolean isShowLoading() {
        return false;
    }

    private void initListener() {
        shenmaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view.getId() == R.id.next_btn) {
                    PayDialog dialog = new PayDialog(getActivity());

                    dialog.setCaiPiaoInfo((CaipiaoInfo) adapter.getItem(position));
//                    dialog.setTimeListener(ShenMaFragment.this);
                    dialog.showChargeDialog(dialog);
                } else if (view.getId() == R.id.tv_history) {
                    CaipiaoInfo caipiaoInfo = (CaipiaoInfo) adapter.getItem(position);
                    GotoActivity(caipiaoInfo);
                }
                return false;
            }
        });

        shenmaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CaipiaoInfo caipiaoInfo = (CaipiaoInfo) adapter.getItem(position);
                GotoActivity(caipiaoInfo);
            }
        });
    }

    private void GotoActivity(CaipiaoInfo caipiaoInfo) {
        Intent intent = new Intent(getActivity(), CaipiaoListActivity.class);
        intent.putExtra("title", caipiaoInfo.getTicket());
        intent.putExtra("ticket_id", caipiaoInfo.getTicketId());
        startActivity(intent);
    }


    public void computeTime() {
        if (timeNum > 12) {
            //TODO
            //分享时间符合要求
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        computeTime();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.PAY_SUCCESS)
            }
    )
    public void getOrderInfo(OrderInfo orderInfo) {
        getData();
    }
}
