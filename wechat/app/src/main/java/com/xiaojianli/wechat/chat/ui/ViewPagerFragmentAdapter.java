package com.xiaojianli.wechat.chat.ui;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> mList;
	public ViewPagerFragmentAdapter(FragmentManager fm,List<Fragment> fragmentlist) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.mList = fragmentlist;
	}
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList != null ? mList.size() : 0;
	}

}
