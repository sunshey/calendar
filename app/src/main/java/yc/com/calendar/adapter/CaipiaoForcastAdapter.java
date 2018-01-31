package yc.com.calendar.adapter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import yc.com.calendar.R;
import yc.com.calendar.activity.NewsActivity;
import yc.com.calendar.activity.NewsDetailActivity;
import yc.com.calendar.bean.CalendarNewsGroupInfo;
import yc.com.calendar.bean.CalendarNewsInfo;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/12 19:49.
 */

public class CaipiaoForcastAdapter extends BaseQuickAdapter<CalendarNewsGroupInfo, BaseViewHolder> {
    public CaipiaoForcastAdapter(List<CalendarNewsGroupInfo> data) {
        super(R.layout.fragment_caipiao_forecast, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CalendarNewsGroupInfo item) {
        helper.setText(R.id.tv_mainTitle, item.getName());

        if (item.getLists() != null && item.getLists().size() > 0) {

            final CalendarNewsInfo calendarNewsInfo = item.getLists().get(0);
            Glide.with(mContext).load(calendarNewsInfo.getImg()).apply(new RequestOptions().error(R.mipmap.every_day_sample)).into((ImageView) helper.getView(R.id.iv_mainView));
            helper.setText(R.id.tv_mainProduce, calendarNewsInfo.getTitle());

            RecyclerView recyclerView = helper.getView(R.id.recyclerView);

            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            HuangLiAdapter huangLiAdapter = new HuangLiAdapter(item.getLists().subList(1, item.getLists().size()), false);
            recyclerView.setAdapter(huangLiAdapter);
            huangLiAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CalendarNewsInfo item1 = (CalendarNewsInfo) adapter.getItem(position);
                    Intent intent = new Intent(mContext, NewsDetailActivity.class);
                    intent.putExtra("title", item.getName());
                    intent.putExtra("id", item1.getId() + "");
                    mContext.startActivity(intent);
                }
            });
            helper.getView(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    intent.putExtra("title", item.getName());
                    intent.putExtra("type_id", item.getId() + "");
                    mContext.startActivity(intent);
                }
            });

            RxView.clicks(helper.getView(R.id.fl_mainView)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    Intent intent = new Intent(mContext, NewsDetailActivity.class);
                    intent.putExtra("title", item.getName());
                    intent.putExtra("id", calendarNewsInfo.getId() + "");
                    mContext.startActivity(intent);
                }
            });
        }

    }
}
