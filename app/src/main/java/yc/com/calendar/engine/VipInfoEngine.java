package yc.com.calendar.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import java.util.List;

import yc.com.calendar.bean.VipInfo;
import yc.com.calendar.constants.NetConstant;
import rx.Observable;

/**
 * Created by wanglin  on 2018/1/22 09:21.
 */

public class VipInfoEngine extends BaseEngin<ResultInfo<List<VipInfo>>> {
    public VipInfoEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.goods_index_url;
    }


    public Observable<ResultInfo<List<VipInfo>>> getVipInfoList() {

        return rxpost(new TypeReference<ResultInfo<List<VipInfo>>>() {
        }.getType(), null, true, true, true);
    }
}
