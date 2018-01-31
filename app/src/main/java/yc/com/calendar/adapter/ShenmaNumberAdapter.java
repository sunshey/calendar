package yc.com.calendar.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.calendar.R;

/**
 * Created by wanglin  on 2018/1/14 10:41.
 */

public class ShenmaNumberAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean mIsRed;

    public ShenmaNumberAdapter(List<String> data, boolean isRed) {
        super(R.layout.shenma_item_number, data);
        this.mIsRed = isRed;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_number, item);
        if (mIsRed) {
            helper.getView(R.id.tv_number).setBackgroundResource(R.drawable.circle_number_red_shape);
        } else {
            helper.getView(R.id.tv_number).setBackgroundResource(R.drawable.circle_number_blue_shape);
        }
    }
}
