
package com.nano.lanshare.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.nano.lanshare.R;

public class BaseActivity extends Activity implements OnPageChangeListener, OnClickListener {
    private ViewPager mViewPager;
    private List<View> mViewList = new ArrayList<View>();
    private List<TextView> mTabs = new ArrayList<TextView>();
    private MainViewPagerAdapter mPagerAdapter;
    private TextView mAppTab;
    private TextView mPhotoTab;
    private TextView mMediaTab;
    private TextView mFileTab;
    private TextView mHistoryTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_base);
        initView();

    }

    /**
     * 初始化，包括添加view到viewpager中
     */
    private void initView() {
        mAppTab = (TextView) findViewById(R.id.leftTab1);
        mAppTab.setOnClickListener(this);
        mTabs.add(mAppTab);

        mPhotoTab = (TextView) findViewById(R.id.leftTab2);
        mPhotoTab.setOnClickListener(this);
        mTabs.add(mPhotoTab);

        mMediaTab = (TextView) findViewById(R.id.leftTab3);
        mMediaTab.setOnClickListener(this);
        mTabs.add(mMediaTab);

        mFileTab = (TextView) findViewById(R.id.leftTab4);
        mFileTab.setOnClickListener(this);
        mTabs.add(mFileTab);

        mHistoryTab = (TextView) findViewById(R.id.leftTab5);
        mHistoryTab.setOnClickListener(this);
        mTabs.add(mHistoryTab);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        addViews2List();
        mPagerAdapter = new MainViewPagerAdapter(mViewList);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        // 设置第一个tab为默认为选中状态
        mViewPager.setCurrentItem(0);
        setTabSelected(mAppTab);
    }

    /**
     * 添加view到ViewPagerList中去
     * 
     * @param view
     */
    private void addViews2List() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        mViewList.add(layoutInflater.inflate(R.layout.test1, null));
        mViewList.add(layoutInflater.inflate(R.layout.test2, null));
    }

    /**
     * 设置当前选中的tab
     * 
     * @param tab
     */
    private void setTabSelected(TextView tab) {
        for (TextView tv : mTabs) {
            if (tab == tv) {
                tv.setSelected(true);
            } else {
                tv.setSelected(false);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {
        setTabSelected(mTabs.get(position));
    }

    @Override
    public void onPageSelected(int arg0) {

    }

    @Override
    public void onClick(View v) {
        if (v == mAppTab) {
            setTabSelected(mAppTab);
        } else if (v == mPhotoTab) {
            setTabSelected(mPhotoTab);
        } else if (v == mMediaTab) {
            setTabSelected(mMediaTab);
        } else if (v == mFileTab) {
            setTabSelected(mFileTab);
        } else if (v == mHistoryTab) {
            setTabSelected(mHistoryTab);
        }

    }
}
