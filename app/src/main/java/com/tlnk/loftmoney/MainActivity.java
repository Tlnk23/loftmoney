package com.tlnk.loftmoney;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Вот это что? в курсе показывали так
        //Это показывали как работать с фрагментами и всё. Тебе надо тут написать код для работы с табами и в зависимости от выбранного таба будет загружаться свой фрагмент (Расходы, дохады или баланс)
        //Вроде пока всё правильно. Теперь тут инициализируй таб (id/tabs), делай адаптер для работы viewpager и к нему подключай фрагменты.
        //Ещё есть вопросы? А почему до этого крашилось приложение? Потому что фрагмент нужно вставлять в вьюху Fragment, а ты её вставлял в ConstraintLayout

        TabLayout tabLayout = findViewById(R.id.tabs);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.expences);
        tabLayout.getTabAt(1).setText(R.string.income);
        tabLayout.getTabAt(2).setText(R.string.balance);
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
                case 1: return new BudgetFragment();
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
