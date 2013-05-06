package com.et.TestFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
	
	private ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        viewPager = (ViewPager) findViewById(R.id.pager);
    }
    public static class MyAdapter extends FragmentPagerAdapter{
    	
    	public MyAdapter(FragmentManager fm) {
			// TODO Auto-generated constructor stub
    		super(fm);
		}
		@Override
		public Fragment getItem(int positions) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}
		
    }
    private static class ChosenListFragment extends ListFragment{
    	
    	static ChosenListFragment newInstance(){
    		ChosenListFragment chosenListFragment = new ChosenListFragment();
    		
    		return chosenListFragment;
    	}
    	
    }
}