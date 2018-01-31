package yc.com.calendar.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import yc.com.calendar.R;
import yc.com.calendar.bean.CaipiaoInfo;

/**
 * Created by wanglin  on 2018/1/23 15:35.
 */

public class CaipiaoListAdapter extends BaseQuickAdapter<CaipiaoInfo, BaseViewHolder> {
    public CaipiaoListAdapter(List<CaipiaoInfo> data) {
        super(R.layout.activity_caipiao_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CaipiaoInfo item) {

        if (!TextUtils.isEmpty(item.getDate())) {
            try {
                String date = TimeUtils.millis2String(Long.parseLong(item.getDate()) * 1000, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
                helper.setText(R.id.tv_weekday, date);
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }

        }
        helper.setText(R.id.tv_periods, String.format(mContext.getString(R.string.periods), item.getPeriod()))
                .setText(R.id.tv_total_money, String.format(mContext.getString(R.string.yuan), item.getMoney()));

        RecyclerView redRecyclerView = helper.getView(R.id.recyclerView_red);
        RecyclerView blueRecyclerView = helper.getView(R.id.recyclerView_blue);
        redRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        blueRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        redRecyclerView.setAdapter(new ShenmaNumberAdapter(item.getContent().getRed(), true));
        blueRecyclerView.setAdapter(new ShenmaNumberAdapter(item.getContent().getBlue(), false));


    }
}
