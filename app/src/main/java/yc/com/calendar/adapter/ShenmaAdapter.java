package yc.com.calendar.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.pay.other.LogUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import yc.com.blankj.utilcode.util.TimeUtils;
import yc.com.calendar.R;
import yc.com.calendar.bean.CaipiaoInfo;
import yc.com.calendar.util.WeekUtil;

/**
 * Created by wanglin  on 2018/1/14 10:28.
 */

public class ShenmaAdapter extends BaseQuickAdapter<CaipiaoInfo, BaseViewHolder> {
    private long mEffectTime;

    public ShenmaAdapter(List<CaipiaoInfo> data) {
        super(R.layout.fragment_shenma_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CaipiaoInfo item) {

        if (!TextUtils.isEmpty(item.getDate())) {
            String date = TimeUtils.millis2String(Long.parseLong(item.getDate()) * 1000, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
            date += " " + WeekUtil.assignDate2Week(date);
            helper.setText(R.id.tv_kaijiang_date, date);
        }
        helper.setText(R.id.tv_variety_title, item.getTicket())
                .setText(R.id.tv_kaijiang_qishu, String.format(mContext.getString(R.string.periods), item.getPeriod()))
                .addOnClickListener(R.id.next_btn).addOnClickListener(R.id.tv_history)
                .setText(R.id.tv_jackpot, String.format(mContext.getString(R.string.yuan), item.getMoney()));

        if (getmEffectTime() == 0) {
            helper.setVisible(R.id.next_btn, true);
            helper.setVisible(R.id.tv_expire, false);
        } else {
            helper.setVisible(R.id.next_btn, false);
            helper.setVisible(R.id.tv_expire, true);
            LogUtils.e("TAG", getmEffectTime());
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String expireDate = TimeUtils.millis2String(getmEffectTime() * 1000, sf);
            helper.setText(R.id.tv_expire, expireDate + " 到期");
        }
        RecyclerView recyclerView = helper.getView(R.id.recyclerView_number);
        RecyclerView blueRecyclerView = helper.getView(R.id.recyclerView_blue_number);

        RecyclerView nextRedRecyclerView = helper.getView(R.id.recyclerView_next_number);
        RecyclerView nextBlueRecyclerView = helper.getView(R.id.recyclerView_next_blue_number);
        if ("福彩3D".equals(item.getTicket())) {
            helper.setVisible(R.id.ll_shiji, true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        blueRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        if (item.getContent() != null) {
            recyclerView.setAdapter(new ShenmaNumberAdapter(item.getContent().getRed(), true));
            blueRecyclerView.setAdapter(new ShenmaNumberAdapter(item.getContent().getBlue(), false));
        }
        nextRedRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        nextBlueRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        if (item.getPredict() != null) {
            nextRedRecyclerView.setAdapter(new ShenmaNumberAdapter(item.getPredict().getRed(), true));
            nextBlueRecyclerView.setAdapter(new ShenmaNumberAdapter(item.getPredict().getBlue(), false));
        }

    }


    public long getmEffectTime() {
        return mEffectTime;
    }

    public void setmEffectTime(long mEffectTime) {
        this.mEffectTime = mEffectTime;
        notifyDataSetChanged();
    }
}
