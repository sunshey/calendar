package calendar.yc.com.calendar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/11 15:46.
 */

public class MainAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;

    public MainAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
