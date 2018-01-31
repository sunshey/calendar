package yc.com.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.pay.other.ScreenUtil;

import java.util.List;

import yc.com.calendar.R;
import yc.com.calendar.bean.VipInfo;

/**
 * Created by wanglin  on 2018/1/22 09:47.
 */

public class GoodInfoAdapter extends BaseQuickAdapter<VipInfo, BaseViewHolder> {
    private SparseArray<View> sparseArray;

    public GoodInfoAdapter(List<VipInfo> data) {
        super(R.layout.dialog_vip_item, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, VipInfo item) {
        helper.setText(R.id.tv_single_buy, item.getPrice())
                .setText(R.id.tv_good_name, item.getName());
        int position = helper.getAdapterPosition();

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();

        layoutParams.width = ScreenUtil.getWidth(mContext) / 5;
        helper.itemView.setLayoutParams(layoutParams);
        sparseArray.put(position, helper.getView(R.id.ll_sign));
        if (position == 0) {
            sparseArray.get(0).setBackgroundResource(R.drawable.vip_selector_bg);
        }

    }

    public View getView(int position) {
        for (int i = 0; i < sparseArray.size(); i++) {
            sparseArray.get(i).setBackgroundResource(R.drawable.vip_normal_bg);
        }

        return sparseArray.get(position);
    }
}
