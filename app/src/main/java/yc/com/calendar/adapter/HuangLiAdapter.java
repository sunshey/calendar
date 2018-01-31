package yc.com.calendar.adapter;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.calendar.R;
import yc.com.calendar.bean.CalendarNewsInfo;

/**
 * Created by wanglin  on 2018/1/12 17:10.
 */

public class HuangLiAdapter extends BaseQuickAdapter<CalendarNewsInfo, BaseViewHolder> {
    private boolean mIsHuangli;

    public HuangLiAdapter(List<CalendarNewsInfo> data, boolean isHuangli) {
        super(R.layout.fragment_huangli_item, data);
        this.mIsHuangli = isHuangli;
    }

    @Override
    protected void convert(BaseViewHolder helper, CalendarNewsInfo item) {


        Glide.with(mContext).load(item.getImg()).apply(new RequestOptions().error(R.mipmap.huangli_item_sample)).into((ImageView) helper.getView(R.id.iv));
        helper.setText(R.id.news_title, item.getTitle())
                .setText(R.id.news_count, String.format(mContext.getString(R.string.preview_count), item.getCount() + ""));
        int position = helper.getAdapterPosition();
        if (!mIsHuangli) {
            if (position == mData.size() - 1) {
                helper.getView(R.id.view).setVisibility(View.GONE);
            }
        }

    }
}
