package calendar.yc.com.calendar.bean;

import java.io.PipedReader;
import java.util.List;

/**
 * Created by wanglin  on 2018/1/12 19:50.
 */

public class CaipiaoNewsInfo {

    private String title;
    private String imageUrl;
    private List<CalendarNewsInfo> iTemInfos;
    private int imgId;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<CalendarNewsInfo> getiTemInfos() {
        return iTemInfos;
    }

    public void setiTemInfos(List<CalendarNewsInfo> iTemInfos) {
        this.iTemInfos = iTemInfos;
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
}
