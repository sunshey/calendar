package yc.com.calendar.bean;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/18 14:06.
 */

public class CalendarNewsGroupInfo {

    private int id;
    private String name;
    private int pid;
    private List<CalendarNewsInfo> lists;

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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<CalendarNewsInfo> getLists() {
        return lists;
    }

    public void setLists(List<CalendarNewsInfo> lists) {
        this.lists = lists;
    }
}
