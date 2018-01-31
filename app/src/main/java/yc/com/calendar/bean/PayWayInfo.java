package yc.com.calendar.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2018/1/18 11:31.
 */

public class PayWayInfo {
    @JSONField(name = "pay_way_name")
    private String paywayName;
    @JSONField(name = "pay_way_title")
    private String paywayTitle;

    public PayWayInfo() {
    }

    public PayWayInfo(String paywayName) {
        this.paywayName = paywayName;
    }

    public String getPaywayName() {
        return paywayName;
    }

    public void setPaywayName(String paywayName) {
        this.paywayName = paywayName;
    }

    public String getPaywayTitle() {
        return paywayTitle;
    }

    public void setPaywayTitle(String paywayTitle) {
        this.paywayTitle = paywayTitle;
    }
}
