package yc.com.calendar.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import yc.com.calendar.bean.CaipiaoInfoWrapper;
import yc.com.calendar.constants.NetConstant;
import rx.Observable;

/**
 * Created by wanglin  on 2018/1/18 14:18.
 */

public class CaipiaoEngine extends BaseEngin<ResultInfo<CaipiaoInfoWrapper>> {
    public CaipiaoEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.predict_index;
    }


    public Observable<ResultInfo<CaipiaoInfoWrapper>> getCaipiaoInfoList() {


        return rxpost(new TypeReference<ResultInfo<CaipiaoInfoWrapper>>() {
        }.getType(), null, true, true, true);
    }
}
