package yc.com.calendar.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2018/1/12 17:12.
 */

public class CalendarNewsInfo {
    private int imgId;
    private String title;
    @JSONField(name = "pv_num")// 浏览人数
    private int count;
    private int id;//详情id;

    private String img;
    private String flag;// 新闻属性 0: 普通; 1: 推荐; 2: 热门
    private int type_id;// 所属分类ID
    private String type_name;// 分类名称，
    private String keywords;// 关键词
    private String add_time;// 添加时间
    private String add_date;// 添加日期

    private String author;
    private String body;// 新闻内容


    public CalendarNewsInfo() {
    }


    public CalendarNewsInfo(int imgId, String title, int count) {
        this.imgId = imgId;
        this.title = title;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
