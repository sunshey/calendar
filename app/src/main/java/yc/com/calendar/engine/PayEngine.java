package yc.com.calendar.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import java.util.HashMap;
import java.util.Map;

import yc.com.calendar.bean.CaipiaoInfo;
import yc.com.calendar.constants.NetConstant;
import rx.Observable;

/**
 * Created by wanglin  on 2018/1/22 11:27.
 */

public class PayEngine extends BaseEngin<ResultInfo<String>> {
    public PayEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.order_pay_url;
    }

    public Observable<ResultInfo<String>> createOrder(String pay_way_name, String goods_id, CaipiaoInfo caipiaoInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("pay_way_name", pay_way_name);
        params.put("goods_id", goods_id);
        params.put("ticket_id", caipiaoInfo.getTicketId());
        params.put("period", caipiaoInfo.getPeriod());
        return rxpost(new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

}
