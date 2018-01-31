package yc.com.calendar.bean;

/**
 * Created by wanglin  on 2018/1/18 12:46.
 */

public class VipInfo {


    private int id;// 商品ID
    private String name;// 商品名称
    private String price;// 商品价格
    private String img;// 图片
    private String desp;// 商品说明
    private String unit;// 单位
    private int use_time_limit;//有效时长
    private int sort;// 排序
    private int status;// 状态 0：下架，1：上架

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getUse_time_limit() {
        return use_time_limit;
    }

    public void setUse_time_limit(int use_time_limit) {
        this.use_time_limit = use_time_limit;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
