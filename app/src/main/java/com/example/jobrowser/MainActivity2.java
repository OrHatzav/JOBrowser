package com.example.jobrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.jobrowser.fragments.ProfileFragment;
import com.example.jobrowser.fragments.SearchFragment;
import com.example.jobrowser.fragments.UploadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SearchFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment frag = null;

            switch (item.getItemId()) {
                case R.id.ic_search:
                    frag = new SearchFragment();
                    break;

                case R.id.ic_profile:
                    frag = new ProfileFragment();
                    break;
            }
            assert frag != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag).commit();
            return false;

        }
    };
}