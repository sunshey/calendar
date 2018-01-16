package calendar.yc.com.calendar.adapter;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxImageUtils;

import java.util.List;

import calendar.yc.com.calendar.R;
import calendar.yc.com.calendar.bean.ShenmaInfo;

/**
 * Created by wanglin  on 2018/1/14 10:28.
 */

public class ShenmaAdapter extends BaseQuickAdapter<ShenmaInfo, BaseViewHolder> {
    public ShenmaAdapter(List<ShenmaInfo> data) {
        super(R.layout.fragment_shenma_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShenmaInfo item) {
        helper.setText(R.id.tv_variety_title, item.getTitle())
                .setText(R.id.tv_kaijiang_qishu, String.format(mContext.getString(R.string.periods), item.getQishu()))
                .setText(R.id.tv_kaijiang_date, item.getDate());
        RecyclerView recyclerView = helper.getView(R.id.recyclerView_number);
        RecyclerView blueRecyclerView = helper.getView(R.id.recyclerView_blue_number);
        if (item.getTitle().equals("福彩3D")) {
            helper.setVisible(R.id.ll_shiji, true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        blueRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ShenmaNumberAdapter(item.getCaipiaoNumberInfo().getRed(), true));
        blueRecyclerView.setAdapter(new ShenmaNumberAdapter(item.getCaipiaoNumberInfo().getBlue(), false));
        recyclerView.addItemDecoration(new MyItemDecoration());
        blueRecyclerView.addItemDecoration(new MyItemDecoration());
    }


    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, RxImageUtils.dip2px(mContext, 10), 0);
        }
    }
}
