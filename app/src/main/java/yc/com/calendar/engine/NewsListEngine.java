package yc.com.calendar.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yc.com.calendar.bean.CalendarNewsInfo;
import yc.com.calendar.constants.NetConstant;
import rx.Observable;

/**
 * Created by wanglin  on 2018/1/18 14:05.
 */

public class NewsListEngine extends BaseEngin<ResultInfo<List<CalendarNewsInfo>>> {
    public NewsListEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.news_list_url;
    }

    public Observable<ResultInfo<List<CalendarNewsInfo>>> getNewsInfoList(String type_id, int flag, int page, int limit) {

        Map<String, String> params = new HashMap<>();
        params.put("type_id", type_id);
        params.put("flag", flag + "");
        params.put("page", page + "");
        params.put("limit", limit + "");
        return rxpost(new TypeReference<ResultInfo<List<CalendarNewsInfo>>>() {
        }.getType(), params, true, true, true);

    }
}
