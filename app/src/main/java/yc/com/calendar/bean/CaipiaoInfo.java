package yc.com.calendar.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2018/1/18 14:22.
 */

public class CaipiaoInfo {
    @JSONField(name = "ticket_id")
    private String ticketId;
    private String ticket;//彩票名称
    private String period;// 期数
    private String money;// 奖池
    private String date;// 开奖日期
    private CaipiaoNumberInfo content;
    private CaipiaoNumberInfo predict;//预测详情


    public String getTicket() {
        return ticket;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CaipiaoNumberInfo getContent() {
        return content;
    }

    public void setContent(CaipiaoNumberInfo content) {
        this.content = content;
    }

    public CaipiaoNumberInfo getPredict() {
        return predict;
    }

    public void setPredict(CaipiaoNumberInfo predict) {
        this.predict = predict;
    }

}
