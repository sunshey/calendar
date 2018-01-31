package yc.com.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.pay.other.ScreenUtil;

import java.util.List;

import yc.com.calendar.R;
import yc.com.calendar.bean.PayWayInfo;

/**
 * Created by wanglin  on 2018/1/23 08:41.
 */

public class PaywayAdapter extends BaseQuickAdapter<PayWayInfo, BaseViewHolder> {
    private SparseArray<View> sparseArray;

    public PaywayAdapter(List<PayWayInfo> data) {
        super(R.layout.dialog_payway_item, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, PayWayInfo item) {
        if (mContext.getString(R.string.wxpay).equals(item.getPaywayTitle())) {
            helper.setImageResource(R.id.iv_payway, R.mipmap.wxpay_bg);
        } else if (mContext.getString(R.string.alipay).equals(item.getPaywayTitle())) {
            helper.setImageResource(R.id.iv_payway, R.mipmap.alipay_bg);
        }

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        layoutParams.width = (int) (ScreenUtil.getWidth(mContext) * 0.32);
        helper.itemView.setLayoutParams(layoutParams);

        int position = helper.getAdapterPosition();
        sparseArray.put(position, helper.getView(R.id.ll_wx));
        getView(0).setBackgroundResource(R.drawable.vip_selector_bg);
    }

    public View getView(int position) {
        for (int i = 0; i < sparseArray.size(); i++) {
            sparseArray.get(i).setBackgroundResource(R.drawable.vip_normal_bg);
        }

        return sparseArray.get(position);
    }
}
