package com.jnu.student.main;

import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.task.DaytaskFragment;
import com.jnu.student.task.NormaltaskFragment;
import com.jnu.student.R;
import com.jnu.student.WeektaskFragment;
import com.jnu.student.data.PointsViewModel;


public class MaintaskFragment extends Fragment {

    private String[] tabHeaderStrings = {"每日任务", "普通任务"};//,"副本任务"


    // 声明一个PointsViewModel的变量
    private PointsViewModel pointsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        // 创建适配器
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(fragmentAdapter);

        // 将TabLayout和ViewPager2进行关联
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabHeaderStrings[position])
        ).attach();

        // 从MainActivity中获取PointsViewModel的实例
        pointsViewModel = ((MainActivity) getActivity()).getPointsViewModel();



    }

    // 创建一个方法，用来返回PointsViewModel的实例
    public PointsViewModel getPointsViewModel() {
        return pointsViewModel;
    }


    public class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 2;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置返回对应的Fragment实例
            switch (position) {
                case 0:
                    return new DaytaskFragment();
                case 1:
                    return new NormaltaskFragment();

                //case 3:
                //return new DaytaskFragment();
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
