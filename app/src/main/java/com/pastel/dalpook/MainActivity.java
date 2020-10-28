package com.pastel.dalpook;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.pastel.dalpook.Fragment.Fragment_Cal;
import com.pastel.dalpook.Fragment.Fragment_Setting;
import com.pastel.dalpook.Fragment.Fragment_Today;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private Fragment_Today fragment_today;
    private Fragment_Cal fragment_cal;
    private Fragment_Setting fragment_setting;

    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);

                /**
                 *  네비게이션 / 뷰페이저 세팅
                 */
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_one:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_two:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_three:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null){
                    prevMenuItem.setChecked(false);
                }else{
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragment_today = new Fragment_Today();
        fragment_cal = new Fragment_Cal();
        fragment_setting = new Fragment_Setting();
        adapter.addFragment(fragment_today, "일정");
        adapter.addFragment(fragment_cal, "달력");
        adapter.addFragment(fragment_setting, "설정");
        viewPager.setAdapter(adapter);
    }
}