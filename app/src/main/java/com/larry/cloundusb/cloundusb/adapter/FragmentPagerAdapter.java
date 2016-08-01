package com.larry.cloundusb.cloundusb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 3/18/2016.
 * <p/>
 * sliddingtab 适配器的滑动页面适配器
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {


    String[] titleName = GetContextUtil.getInstance().getResources().getStringArray(R.array.slidingTablayoutTitle); //用于标题名
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    FragmentManager manager;

    public FragmentPagerAdapter(FragmentManager manager, List<Fragment> list) {
        super(manager);
        fragmentList = list;
        this.manager = manager;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);

    }

    @Override
    public CharSequence getPageTitle(int position) {
            return titleName[position];
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //  super.destroyItem(container, position, object);
        //阻止了fragment被销毁


    }


     /*
    *
    * 刷新界面接口
    * */

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


}
