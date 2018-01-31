package yc.com.calendar.bean;

/**
 * Created by wanglin  on 2018/1/15 11:23.
 */

public class HuangLiDbInfo {

    private int jx;
    private int gz;
    private String ji;
    private String yi;

    public HuangLiDbInfo() {
    }

    public HuangLiDbInfo(String ji, String yi) {
        this.ji = ji;
        this.yi = yi;
    }

    public int getJx() {
        return jx;
    }

    public void setJx(int jx) {
        this.jx = jx;
    }

    public int getGz() {
        return gz;
    }

    public void setGz(int gz) {
        this.gz = gz;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getYi() {
        return yi;
    }

    public void setYi(String yi) {
        this.yi = yi;
    }
}
