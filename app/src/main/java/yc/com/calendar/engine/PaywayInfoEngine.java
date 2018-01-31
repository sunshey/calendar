package yc.com.calendar.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import java.util.List;

import yc.com.calendar.bean.PayWayInfo;
import yc.com.calendar.constants.NetConstant;
import rx.Observable;

/**
 * Created by wanglin  on 2018/1/18 16:51.
 */

public class PaywayInfoEngine extends BaseEngin<ResultInfo<List<PayWayInfo>>> {
    public PaywayInfoEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.payway_url;
    }

    public Observable<ResultInfo<List<PayWayInfo>>> getPayWayInfoList() {

        return rxpost(new TypeReference<ResultInfo<List<PayWayInfo>>>() {
        }.getType(), null, true, true, true);

    }
}
