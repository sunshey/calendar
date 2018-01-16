package calendar.yc.com.calendar.bean;

/**
 * Created by wanglin  on 2018/1/12 17:12.
 */

public class CalendarNewsInfo {
    private String imagurl;
    private int imgId;
    private String title;
    private int count;
    private int id;

    public CalendarNewsInfo() {
    }



    public CalendarNewsInfo(String imagurl, String title, int count) {
        this.imagurl = imagurl;
        this.title = title;
        this.count = count;
    }

    public CalendarNewsInfo(int imgId, String title, int count) {
        this.imgId = imgId;
        this.title = title;
        this.count = count;
    }

    public String getImagurl() {
        return imagurl;
    }

    public void setImagurl(String imagurl) {
        this.imagurl = imagurl;
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
}
