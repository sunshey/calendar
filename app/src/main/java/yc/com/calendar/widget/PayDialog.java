package yc.com.calendar.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;
import com.kk.pay.PayImplFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import yc.com.calendar.R;
import yc.com.calendar.adapter.GoodInfoAdapter;
import yc.com.calendar.adapter.PaywayAdapter;
import yc.com.calendar.bean.CaipiaoInfo;
import yc.com.calendar.bean.PayWayInfo;
import yc.com.calendar.bean.VipInfo;
import yc.com.calendar.constants.BusAction;
import yc.com.calendar.constants.NetConstant;
import yc.com.calendar.constants.PayConfig;
import yc.com.calendar.util.PaywayInfoHelper;
import yc.com.calendar.util.VipInfoHelper;
import rx.functions.Action1;

public class PayDialog extends BaseDialog {


    private Dialog dialog;


    private List<PayWayInfo> payWayInfos;

    private TextView tvPayPrice;
    private LinearLayout llPay;
    private String wx_pay = PayConfig.wx_pay;
    private String ali_pay = PayConfig.ali_pay;

    private IPayAbs iPayAbs;

    private List<VipInfo> vipInfos;
    private RecyclerView goodRecyclerView;
    private RecyclerView paywayRecyclerView;
    private VipInfo vipInfo;
    private GoodInfoAdapter goodInfoAdapter;

    private CaipiaoInfo caiPiaoInfo;

    private static final int ALI_PAY = 1;//支付宝支付
    private static final int WX_PAY = 2;//微信支付

    private int pay_way = WX_PAY;//选择支付方式
    private PaywayAdapter paywayAdapter;


    public PayDialog(@NonNull Activity context) {
        super(context);
    }


    @Override
    protected void getView(View view) {
        vipInfos = VipInfoHelper.getGoodInfoList();
        payWayInfos = PaywayInfoHelper.getPayWayInfoList();
        initView(view);
        initListener();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pay_dialog;
    }


    private void initListener() {

        goodInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                goodInfoAdapter.getView(position).setBackgroundResource(R.drawable.vip_selector_bg);
                vipInfo = (VipInfo) adapter.getItem(position);
                tvPayPrice.setText(String.format(mContext.getString(R.string.pay_money), vipInfo.getPrice()));
            }
        });
        paywayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                paywayAdapter.getView(position).setBackgroundResource(R.drawable.vip_selector_bg);
                PayWayInfo payWayInfo = (PayWayInfo) adapter.getItem(position);
                if (payWayInfo.getPaywayTitle().equals(mContext.getString(R.string.wxpay))) {
                    pay_way = WX_PAY;
                } else {
                    pay_way = ALI_PAY;
                }
            }
        });

        RxView.clicks(llPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {//支付
                switch (pay_way) {
                    case ALI_PAY:
                        iPayAbs.alipay(getOrderParamsInfo(vipInfo.getId() + "", caiPiaoInfo.getTicketId(), caiPiaoInfo.getPeriod(), ali_pay), new MyCallBack());
                        break;
                    case WX_PAY:
                        iPayAbs.wxpay(getOrderParamsInfo(vipInfo.getId() + "", caiPiaoInfo.getTicketId(), caiPiaoInfo.getPeriod(), wx_pay), new MyCallBack());
                }

            }
        });

    }

    private void initView(View view) {

        for (PayWayInfo payWayInfo : payWayInfos) {
            if (payWayInfo.getPaywayTitle().equals(mContext.getString(R.string.wxpay))) {
                wx_pay = payWayInfo.getPaywayName();
            } else {
                ali_pay = payWayInfo.getPaywayName();
            }
        }

        iPayAbs = new I1PayAbs(mContext, PayImplFactory.createPayImpl(mContext, ali_pay), PayImplFactory
                .createPayImpl(mContext, wx_pay));

        goodRecyclerView = view.findViewById(R.id.recyclerView_good);
        paywayRecyclerView = view.findViewById(R.id.recyclerView_payway);

        tvPayPrice = view.findViewById(R.id.tv_pay_price);
        llPay = view.findViewById(R.id.ll_pay);
        if (vipInfos != null && vipInfos.size() > 0) {
            vipInfo = vipInfos.get(0);
        }
        goodRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        goodInfoAdapter = new GoodInfoAdapter(vipInfos);
        goodRecyclerView.setAdapter(goodInfoAdapter);
        goodRecyclerView.addItemDecoration(new MyItemDecoration());

        paywayRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        paywayAdapter = new PaywayAdapter(payWayInfos);
        paywayRecyclerView.setAdapter(paywayAdapter);
        paywayRecyclerView.addItemDecoration(new MyItemDecoration());
        tvPayPrice.setText(String.format(mContext.getString(R.string.pay_money), vipInfo.getPrice()));

    }

    public void showChargeDialog(Dialog dia) {
        this.dialog = dia;
        this.dialog.show();
    }

    public void closeChargeDialog() {
        if (isValidContext(mContext) && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @SuppressLint("NewApi")
    private boolean isValidContext(Context ctx) {
        Activity activity = (Activity) ctx;

        if (Build.VERSION.SDK_INT > 17) {
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (activity == null || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        }
    }

    public void setCaiPiaoInfo(CaipiaoInfo caiPiaoInfo) {
        this.caiPiaoInfo = caiPiaoInfo;
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, SizeUtils.dp2px(12), 0);
        }
    }


    private OrderParamsInfo getOrderParamsInfo(String goodId, String ticketId, String period, String payWayName) {

        return new OrderParamsInfo(NetConstant.order_pay_url, goodId, ticketId, period, payWayName);

    }


    private class MyCallBack implements IPayCallback {
        @Override
        public void onSuccess(OrderInfo orderInfo) {
            dismiss();
            RxBus.get().post(BusAction.PAY_SUCCESS, orderInfo);

        }

        @Override
        public void onFailure(OrderInfo orderInfo) {

        }
    }
}