package com.example.shravanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;




import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    private ViewPager2 viewPager2;
    Fragment home_frag, bt_frag, sos_frag, settings_frag, face_frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Navigation bar functionality
        navigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.viewpager2);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()){

                    case R.id.nav_home:
                        //fragment = new HomeFragment();
                        viewPager2.setCurrentItem(0,false);
                        break;

                    case R.id.nav_face:
                        //fragment = new FaceFragment();
                        viewPager2.setCurrentItem(4,false);
                        break;

                    case R.id.nav_setting:
                        //fragment = new SettingsFragment();
                        viewPager2.setCurrentItem(1,false);
                        break;

                    case R.id.nav_sos:
                        //fragment = new SosFragment();
                        viewPager2.setCurrentItem(2,false);
                        break;

                    case R.id.nav_bluetooth:
                        //fragment = new BtFragment();
                        viewPager2.setCurrentItem(3,false);
                        break;

                }
                //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return false;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.nav_setting).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.nav_sos).setChecked(true);
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.nav_bluetooth).setChecked(true);
                        break;
                    case 4:
                        navigationView.getMenu().findItem(R.id.nav_face).setChecked(true);
                        break;

                }
            }
        });

        // on loadup, Home fragment is loaded up
        if (savedInstanceState == null) {
            viewPager2.setCurrentItem(0,false); // change to whichever id should be default
        }

        setupViewPager(viewPager2);

    }

    private void setupViewPager(ViewPager2 viewPager2) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        home_frag =new HomeFragment();
        settings_frag =new SettingsFragment();
        sos_frag = new SosFragment();
        bt_frag = new BtFragment();
        face_frag = new FaceFragment();


        adapter.addFragment(home_frag);
        adapter.addFragment(settings_frag);
        adapter.addFragment(sos_frag);
        adapter.addFragment(bt_frag);
        adapter.addFragment(face_frag);

        viewPager2.setAdapter(adapter);

    }
}