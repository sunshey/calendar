package yc.com.calendar.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import java.util.List;

import yc.com.calendar.bean.CalendarNewsGroupInfo;
import yc.com.calendar.constants.NetConstant;
import rx.Observable;

/**
 * Created by wanglin  on 2018/1/18 14:12.
 */

public class NewsGroupEngine extends BaseEngin<ResultInfo<List<CalendarNewsGroupInfo>>> {
    public NewsGroupEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.news_group;
    }

    public Observable<ResultInfo<List<CalendarNewsGroupInfo>>> getNewsGroupInfo() {

        return rxpost(new TypeReference<ResultInfo<List<CalendarNewsGroupInfo>>>() {
        }.getType(), null, true, true, true);

    }
}
