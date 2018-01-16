package calendar.yc.com.calendar.bean;

/**
 * Created by wanglin  on 2018/1/15 11:26.
 */

public class IndexDate {
    private String date;
    private int rowid;
    private String jx;
    private String gz;

    public IndexDate() {
    }

    public IndexDate(String jx, String gz) {
        this.jx = jx;
        this.gz = gz;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }


    public void setJx(String jx) {
        this.jx = jx;
    }

    public void setGz(String gz) {
        this.gz = gz;
    }

    public String getJx() {
        return jx;
    }

    public String getGz() {
        return gz;
    }
}
