package yc.com.calendar.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yc.com.calendar.bean.CaipiaoInfo;
import yc.com.calendar.constants.NetConstant;
import rx.Observable;

/**
 * Created by wanglin  on 2018/1/23 15:43.
 */

public class CaipiaoListEngine extends BaseEngin<ResultInfo<List<CaipiaoInfo>>> {
    public CaipiaoListEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.ticket_history_url;
    }

    public Observable<ResultInfo<List<CaipiaoInfo>>> getCaipiaoList(String ticket_id, String period, int page, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("ticket_id", ticket_id);
        params.put("period", period);
        params.put("page", page + "");
        params.put("limit", limit + "");
        return rxpost(new TypeReference<ResultInfo<List<CaipiaoInfo>>>() {
        }.getType(), params, true, true, true);

    }
}
