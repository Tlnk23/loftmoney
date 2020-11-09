package com.tlnk.loftmoney;


import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        FloatingActionButton btnAdd = findViewById(R.id.addNewExpense);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int activeFragmentIndex = viewPager.getCurrentItem();
                Fragment activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);
                    Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                    intent.putExtra("activeFragmentIndex", activeFragmentIndex);
                    activeFragment.startActivityForResult(intent, 0);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.expences);
        tabLayout.getTabAt(1).setText(R.string.income);
        tabLayout.getTabAt(2).setText(R.string.balance);
    }

    @Override
    public void onActionModeStarted(final ActionMode mode) {
        super.onActionModeStarted(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue));
    }

    @Override
    public void onActionModeFinished(final ActionMode mode) {
        super.onActionModeFinished(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return new BudgetFragment();
                case 1: return new BudgetFragment_2();
                case 2: return new BudgetFragment();
                default: return new BudgetFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
