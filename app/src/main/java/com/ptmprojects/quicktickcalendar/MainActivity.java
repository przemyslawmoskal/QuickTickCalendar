package com.ptmprojects.quicktickcalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.ptmprojects.quicktickcalendar.util.TimeUtils;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String DATE_AT_SHOW_UP = "MainActivity.Date";
    private static Context mContext;
    private ViewPager vpPager;
    private PagerAdapter adapterViewPager;
    private Map<Integer, String> mFragmentsTags;
    private String dateString;
    DateTimeFormatter mDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    public ViewPager getVpPager() {
        return vpPager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task_pager);
        Intent intent = getIntent();
        if (intent.hasExtra(DATE_AT_SHOW_UP)) {
            dateString = intent.getStringExtra(DATE_AT_SHOW_UP);
        } else {
            dateString = null;
        }
        mContext = this;
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((MyPagerAdapter) vpPager.getAdapter()).getFragment(position);
                if (fragment != null) {
                    fragment.onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vpPager.setAdapter(adapterViewPager);

        if (dateString == null) {
            vpPager.setCurrentItem(TimeUtils.getPositionForDay(new LocalDate()));
        } else {
            LocalDate dateToSet = new LocalDate(dateString);
            vpPager.setCurrentItem(TimeUtils.getPositionForDay(dateToSet));
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        private FragmentManager mFragmentManager;

        private Calendar cal;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mFragmentsTags = new HashMap<>();
        }

        @Override
        public int getCount() {
            return TimeUtils.DAYS_OF_TIME;
        }

        @Override
        public Fragment getItem(int position) {
            LocalDate timeForPosition = TimeUtils.getDayForPosition(position);
            return SingleDayFragment.newInstance(timeForPosition);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            LocalDate dateToShowOnTabs = TimeUtils.getDayForPosition(position);
            if ((TimeUtils.getPositionForDay(new LocalDate()) - 2) == position) {
                return getString(R.string.day_before_yesterday);
            } else if ((TimeUtils.getPositionForDay(new LocalDate()) - 1) == position) {
                return getString(R.string.yesterday);
            } else if ((TimeUtils.getPositionForDay(new LocalDate())) == position) {
                return getString(R.string.today);
            } else if ((TimeUtils.getPositionForDay(new LocalDate()) + 1) == position) {
                return getString(R.string.tomorrow);
            } else if ((TimeUtils.getPositionForDay(new LocalDate()) + 2) == position) {
                return getString(R.string.day_after_tomorrow);
            } else {
                return TimeUtils.getFormattedDate(mContext, dateToShowOnTabs);
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentsTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentsTags.get(position);
            if (tag == null) {
                return null;
            }
            return mFragmentManager.findFragmentByTag(tag);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

    }
}
