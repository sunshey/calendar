package yc.com.calendar.constants;

/**
 * Created by wanglin  on 2018/1/17 10:48.
 */

public interface NetConstant {


    boolean isDebug = Config.DEBUG;

    String debug_base_url = "http://en.qqtn.com/api/";

    String baser_url = "http://c.bshu.com/v1/";

    //新闻详情
    String news_info_url = (isDebug ? debug_base_url : baser_url) + "news/info";
    //新闻列表
    String news_list_url = (isDebug ? debug_base_url : baser_url) + "news/index";
    //新闻组
    String news_group = (isDebug ? debug_base_url : baser_url) + "news/group";
    //彩票号码预测
    String predict_index = (isDebug ? debug_base_url : baser_url) + "predict/index";

    //支付方式
    String payway_url = (isDebug ? debug_base_url : baser_url) + "order/payWay";

    //商品列表
    String goods_index_url = (isDebug ? debug_base_url : baser_url) + "goods/index";

    //创建订单
    String order_pay_url = (isDebug ? debug_base_url : baser_url) + "order/pay";

    //历史开奖
    String ticket_history_url = (isDebug ? debug_base_url : baser_url) + "ticket/history";

}
