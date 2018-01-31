package yc.com.calendar.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;


import java.util.HashMap;
import java.util.Map;


import yc.com.calendar.bean.CalendarNewsInfo;
import yc.com.calendar.constants.NetConstant;
import rx.Observable;

/**
 * Created by wanglin  on 2018/1/18 13:54.
 */

public class NewsDetailEngine extends BaseEngin<ResultInfo<CalendarNewsInfo>> {
    public NewsDetailEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.news_info_url;
    }

    public Observable<ResultInfo<CalendarNewsInfo>> getNewsInfoDetail(String news_id) {

        Map<String, String> params = new HashMap<>();
        params.put("news_id", news_id);
        return rxpost(new TypeReference<ResultInfo<CalendarNewsInfo>>() {
        }.getType(), params, true, true, true);

    }
}
