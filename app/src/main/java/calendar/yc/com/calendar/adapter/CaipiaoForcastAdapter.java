package calendar.yc.com.calendar.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import calendar.yc.com.calendar.R;
import calendar.yc.com.calendar.bean.CaipiaoNewsInfo;

/**
 * Created by wanglin  on 2018/1/12 19:49.
 */

public class CaipiaoForcastAdapter extends BaseQuickAdapter<CaipiaoNewsInfo, BaseViewHolder> {
    public CaipiaoForcastAdapter(List<CaipiaoNewsInfo> data) {
        super(R.layout.fragment_caipiao_forecast, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CaipiaoNewsInfo item) {
        helper.setText(R.id.tv_mainTitle, item.getTitle());
        Glide.with(mContext).load(BitmapFactory.decodeResource(mContext.getResources(), item.getImgId())).into((ImageView) helper.getView(R.id.iv_mainView));
        RecyclerView recyclerView = helper.getView(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new HuangLiAdapter(item.getiTemInfos(), false));

    }
}
