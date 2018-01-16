package calendar.yc.com.calendar.bean;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/14 10:29.
 */

public class ShenmaInfo {
    private String title;
    private String qishu;
    private String date;
    private CaipiaoNumberInfo CaipiaoNumberInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQishu() {
        return qishu;
    }

    public void setQishu(String qishu) {
        this.qishu = qishu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public calendar.yc.com.calendar.bean.CaipiaoNumberInfo getCaipiaoNumberInfo() {
        return CaipiaoNumberInfo;
    }

    public void setCaipiaoNumberInfo(calendar.yc.com.calendar.bean.CaipiaoNumberInfo caipiaoNumberInfo) {
        CaipiaoNumberInfo = caipiaoNumberInfo;
    }
}
