package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    ListFragment listFragment;
    SearchFragment searchFragment;
    MyPageFragment MyPageFragment;

    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchFragment=new SearchFragment();
        listFragment=new ListFragment();
        MyPageFragment=new MyPageFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.search:
                                Toast.makeText(getApplicationContext(),"검색 페이지입니다.",Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container,searchFragment).commit();

                                return true;
                            case R.id.list:
                                Toast.makeText(getApplicationContext(),"메인 홈 페이지입니다.",Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container,listFragment).commit();

                                return true;
                            case R.id.profile:
                                Toast.makeText(getApplicationContext(),"프로필 페이지입니다.",Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container,MyPageFragment).commit();

                                return true;
                        }
                        return false;
                    }
                }
        );
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position=tab.getPosition();
        if(position==0){
            bottomNavigation.setSelectedItemId(R.id.search);
        }else if(position==1){
            bottomNavigation.setSelectedItemId(R.id.list);
        }else if(position==2){
            bottomNavigation.setSelectedItemId(R.id.profile);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}