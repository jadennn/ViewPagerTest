package com.example.viewpagertest;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * date: 2018/9/18
 * description: ViewPagerAdapter
 **/
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> viewList;//数据源

    public ViewPagerAdapter(List<View> list){
        viewList = list;
    }

    @Override
    public int getCount() {
        return viewList==null?0:viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if(viewList != null) {
            container.removeView(viewList.get(position));
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if(viewList != null) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
        return super.instantiateItem(container, position);
    }
}
