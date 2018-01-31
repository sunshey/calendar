package yc.com.calendar.bean;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/18 14:22.
 */

public class CaipiaoInfoWrapper {


    private long effect_time;
    private List<CaipiaoInfo> lists;


    public long getEffect_time() {
        return effect_time;
    }

    public void setEffect_time(long effect_time) {
        this.effect_time = effect_time;
    }

    public List<CaipiaoInfo> getLists() {
        return lists;
    }

    public void setLists(List<CaipiaoInfo> lists) {
        this.lists = lists;
    }
}
