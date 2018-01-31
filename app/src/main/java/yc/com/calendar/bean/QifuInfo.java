package yc.com.calendar.bean;

/**
 * Created by wanglin  on 2018/1/19 10:55.
 */

public class QifuInfo {

    private int imgId;
    private String name;

    public QifuInfo() {
    }

    public QifuInfo(int imgId, String name) {
        this.imgId = imgId;
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
