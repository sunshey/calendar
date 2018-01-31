package yc.com.calendar.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.calendar.R;
import yc.com.calendar.bean.QifuInfo;

/**
 * Created by wanglin  on 2018/1/19 10:54.
 */

public class QifuAdapter extends BaseQuickAdapter<QifuInfo, BaseViewHolder> {
    public QifuAdapter(List<QifuInfo> data) {
        super(R.layout.fragment_qifu_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QifuInfo item) {

        Glide.with(mContext).load(item.getImgId()).into((ImageView) helper.getView(R.id.iv_deng));

        helper.setText(R.id.tv_deng, item.getName());
        int position = helper.getAdapterPosition();
        if (position % 3 == 0) {
            helper.setBackgroundRes(R.id.ll_deng, R.mipmap.qfmd_table_item_left);
        } else if (position % 3 == 1) {
            helper.setBackgroundRes(R.id.ll_deng, R.mipmap.qfmd_table_item_center);
        } else if (position % 3 == 2) {
            helper.setBackgroundRes(R.id.ll_deng, R.mipmap.qfmd_table_item_right);
        }
    }
}
