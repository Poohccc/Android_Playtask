package com.jnu.student.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.R;
import com.jnu.student.RewardFragment;
import com.jnu.student.WeektaskFragment;
import com.jnu.student.data.PointsViewModel;
import com.jnu.student.webFragment;


public class MainActivity extends AppCompatActivity {

    private String[] tabHeaderStrings = {"任务","奖励", "统计"};//,"副本任务"
    private PointsViewModel pointsViewModel;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // 创建适配器
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(fragmentAdapter);
        // 将TabLayout和ViewPager2进行关联
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabHeaderStrings[position])
        ).attach();


        // 使用ViewModelProvider获取PointsViewModel的实例
        pointsViewModel = new ViewModelProvider(this).get(PointsViewModel.class);
    }


    // 创建一个方法，用来返回PointsViewModel的实例
    public PointsViewModel getPointsViewModel() {
        return pointsViewModel;
    }

    public class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 3;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置返回对应的Fragment实例
            switch (position) {
                case 0:
                    return new MaintaskFragment();
                case 1:
                    return new RewardFragment();
                case 2:
                    return new webFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }
}