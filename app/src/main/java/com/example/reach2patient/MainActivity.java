package com.example.reach2patient;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.example.reach2patient.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
//        Log.d(TAG, "onCreate: " + String.valueOf(tabs.getTabCount()));
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.add_post);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.faq:
                intent = new Intent(getBaseContext(), FAQActivity.class);
                startActivity(intent);
                break;
            case R.id.contact:
                intent = new Intent(getBaseContext(), ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.tnc:
                intent = new Intent(getBaseContext(), TermsActivity.class);
                startActivity(intent);
                break;
            case R.id.consent:
                intent = new Intent(getBaseContext(), ConsentActivity.class);
                startActivity(intent);
                break;
            case R.id.test:
                intent = new Intent(getBaseContext(), TestActivity.class);
                startActivity(intent);
                break;
//            case R.id.call:
//
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}