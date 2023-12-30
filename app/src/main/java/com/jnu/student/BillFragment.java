package com.jnu.student;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jnu.student.data.DayTaskItem;
import com.jnu.student.data.PointsViewModel;


import java.text.ParseException;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class BillFragment extends Fragment {



    public static BillFragment newInstance(String param1, String param2) {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    // 假设你有一个 RecyclerView 来显示任务和日期的列表
    private RecyclerView recycle_view_bill;

    private BillAdapter billAdapter;
    private PointsViewModel billViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bill, container, false);

        // 获取RecyclerView控件
        recycle_view_bill = rootView.findViewById(R.id.recycle_view_bill);
        // 为线性布局
        recycle_view_bill.setLayoutManager(new LinearLayoutManager(requireActivity()));

        billAdapter = new BillAdapter(new ArrayList<>());


        // Get the shared ViewModel from the activity
        billViewModel = new ViewModelProvider(requireActivity()).get(PointsViewModel.class);

        billViewModel.getBillItems().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<DayTaskItem>>() {
            @Override
            public void onChanged(ObservableList<DayTaskItem> sender) { updateUI(); }

            @Override
            public void onItemRangeChanged(ObservableList<DayTaskItem> sender, int positionStart, int itemCount) { updateUI(); }


            @Override
            public void onItemRangeInserted(ObservableList<DayTaskItem> sender, int positionStart, int itemCount) { updateUI(); }

            @Override
            public void onItemRangeMoved(ObservableList<DayTaskItem> sender, int fromPosition, int toPosition, int itemCount) {
                updateUI();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<DayTaskItem> sender, int positionStart, int itemCount) { updateUI(); }
        });

        return rootView;
    }


    // 根据账单项更新RecyclerView的UI
    private void updateUI() {
        // 从view model中获取账单项
        List<DayTaskItem> billItems =  billViewModel.getBillItems();

        // 使用一个HashMap按日期分组账单项
        HashMap<Date, List<DayTaskItem>> billMap = new HashMap<>();
        for (DayTaskItem billItem : billItems) {
            Date date = billItem.getDate();
            if (!billMap.containsKey(date)) {
                billMap.put(date, new ArrayList<>());
            }
            billMap.get(date).add(billItem);
        }

        // 创建一个列表来显示在RecyclerView上
        List<Object> displayItems = new ArrayList<>();
        for (Date date : billMap.keySet()) {
            displayItems.add(date); // 添加日期作为标题
            displayItems.addAll(billMap.get(date)); // 添加该日期下的账单项
        }

        // 为RecyclerView创建一个适配器
        BillAdapter adapter = new BillAdapter(displayItems);
        recycle_view_bill.setAdapter(adapter);
    }






    // 为RecyclerView创建一个适配器
    public class BillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_DATE = 0; // 日期标题的视图类型
        private static final int TYPE_BILL = 1; // 账单项的视图类型

        private static final int TYPE_BILL_FIRST=2;
        private static final int TYPE_BILL_OTHER=3;



        private List<Object> displayItems; // 要显示的列表

        // 构造方法
        public BillAdapter(List<Object> displayItems) {
            this.displayItems = displayItems;
        }

        // 获取给定位置的视图类型
        @Override
        public int getItemViewType(int position) {
            Object item = displayItems.get(position);
            if (item instanceof Date) {
                return TYPE_DATE;
            } else if (item instanceof DayTaskItem) {

                if (position > 0 && displayItems.get(position - 1) instanceof Date) {

                    return TYPE_BILL_FIRST;
                } else {
                    return  TYPE_BILL_OTHER;
                }

            } else {
                return -1; // 无效类型
            }
        }

        // 为给定的视图类型创建一个视图持有者
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == TYPE_DATE) {
                // 填充一个日期标题的布局
                View view = inflater.inflate(R.layout.date_header, parent, false);
                return new DateViewHolder(view);
            } else if (viewType ==  TYPE_BILL_FIRST) {
                // 填充一个账单项的布局
                View view = inflater.inflate(R.layout.item_header, parent, false);
                return new BillViewHolder(view);
            }else if (viewType == TYPE_BILL_OTHER) {
                // 填充一个其他账单项的布局，它只包含了任务名称和积分
                View view = inflater.inflate(R.layout.item_header, parent, false);
                return new BillViewHolder(view);
            } else {
                return null; // 无效类型
            }
        }

        // 将数据绑定到一个视图持有者
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Object item = displayItems.get(position);
            if (holder instanceof DateViewHolder) {
                // 在日期标题中显示日期
                DateViewHolder dateHolder = (DateViewHolder) holder;
                Date date = (Date) item;



                // 创建一个SimpleDateFormat对象，指定日期格式为"yyyy-MM-dd"
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                // 将date属性转换成字符串，只保留年月日部分
                String dateString = sdf.format(date);
                // 将字符串转换回Date对象，赋值给新的变量
                Date newDate = null;
                try {
                    newDate = sdf.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dateHolder.dateTextView.setText(sdf.format(newDate));

            } else if (holder instanceof BillViewHolder) {
                // 在账单项中显示任务名称、积分和时间
                BillViewHolder billHolder = (BillViewHolder) holder;
                DayTaskItem billItem = (DayTaskItem) item;
                billHolder.taskTextView.setText(billItem.getName());
                billHolder.pointsTextView.setText(String.valueOf(billItem.getAchievement_Points()));



                // 创建一个SimpleDateFormat对象，指定时间格式为"HH:mm:ss"
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                // 将date属性转换成字符串，只保留年月日部分
                String dateString = sdf.format(billItem.getTime());
                // 将字符串转换回Date对象，赋值给新的变量
                Date newTime = null;
                try {
                    newTime = sdf.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                billHolder.timeTextView.setText(sdf.format(newTime));





            }
        }

        // 获取要显示的项目数量
        @Override
        public int getItemCount() {
            return displayItems.size();
        }

        // 一个日期标题的视图持有者
        public class DateViewHolder extends RecyclerView.ViewHolder {
            TextView dateTextView;

            public DateViewHolder(View itemView) {
                super(itemView);
                dateTextView = itemView.findViewById(R.id.date_text_view);
            }
        }

        // 一个账单项的视图持有者
        public class BillViewHolder extends RecyclerView.ViewHolder {
            TextView taskTextView;
            TextView pointsTextView;
            TextView timeTextView;

            public BillViewHolder(View itemView) {
                super(itemView);
                taskTextView = itemView.findViewById(R.id.task_text_view);
                pointsTextView = itemView.findViewById(R.id.points_text_view);
                timeTextView = itemView.findViewById(R.id.time_text_view);
            }
        }




    }

}


/*
    // 创建一个TaskGroupAdapter类，用来绑定你的RecyclerView和你的TaskGroup列表

    public class TaskGroupAdapter extends RecyclerView.Adapter<TaskGroupAdapter.TaskGroupViewHolder> {
        private List<TaskGroup> taskGroups; // TaskGroup列表

        public TaskGroupAdapter(List<TaskGroup> taskGroups) {
            this.taskGroups = taskGroups;

        }

        @NonNull
        @Override
        public TaskGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 从布局文件中创建视图
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_group_item, parent, false);
            return new TaskGroupViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskGroupViewHolder holder, int position) {
            // 绑定数据到视图
            TaskGroup taskGroup = taskGroups.get(position); // 获取当前的TaskGroup
            holder.dateView.setText(taskGroup.getDate()); // 设置日期
            //holder.taskItemsAdapter = new DaytaskFragment.TaskItemsAdapter(taskGroup.getTasks()); // 创建一个TaskItemsAdapter

            holder.taskItemsAdapter = new TaskItemsAdapter(taskGroup.getTasks()); // 创建一个TaskItemsAdapter
            holder.recyclerView.setAdapter(holder.taskItemsAdapter); // 设置RecyclerView的适配器
        }

        @Override
        public int getItemCount() {
            // 返回TaskGroup列表的大小
            return taskGroups.size();
        }

        // 定义一个内部类，表示每个列表项的视图
        public  class TaskGroupViewHolder extends RecyclerView.ViewHolder {
            TextView dateView; // 日期视图
            RecyclerView recyclerView; // RecyclerView视图
           TaskItemsAdapter taskItemsAdapter; // TaskItemsAdapter对象

            public TaskGroupViewHolder(@NonNull View itemView) {
                super(itemView);
                dateView = itemView.findViewById(R.id.date_view);
                recyclerView = itemView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext())); // 设置RecyclerView的布局管理器
            }
        }



        // 创建一个内部类，用来绑定你的RecyclerView和你的Task列表
        public class TaskItemsAdapter extends RecyclerView.Adapter<TaskItemsAdapter.TaskItemViewHolder> {
            private List<DayTaskItem> tasks; // Task列表

            public TaskItemsAdapter(List<DayTaskItem> task) {
                tasks = task;
            }

            @NonNull
            @Override
            public TaskItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // 从布局文件中创建视图
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daytask, parent, false);
                return new TaskItemViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull TaskItemViewHolder holder, int position) {
                // 绑定数据到视图
                DayTaskItem task = tasks.get(position); // 获取当前的Task
                holder.taskNameView.setText(tasks.get(position).getName()); // 设置任务名称
                holder.taskPointsView.setText((tasks.get(position).getAchievement_Points()+ "")); // 设置任务积分
                // 其他视图的设置省略
            }

            @Override
            public int getItemCount() {
                // 返回Task列表的大小
                return tasks.size();
            }

            // 定义一个内部类，表示每个列表项的视图
            public class TaskItemViewHolder extends RecyclerView.ViewHolder {
                TextView taskNameView; // 任务名称视图
                TextView taskPointsView; // 任务积分视图
                // 其他视图的声明省略

                public TaskItemViewHolder(@NonNull View itemView) {
                    super(itemView);
                    taskNameView = itemView.findViewById(R.id.text_view_task_title);
                    taskPointsView = itemView.findViewById(R.id.text_view_point);
                    // 其他视图的初始化省略
                }
            }
        }




    }
*/
/*

    // 创建一个BillAdapter类来绑定你的RecyclerView和你的任务列表
    public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
        private List<DayTaskItem> tasks; // 任务列表

        public BillAdapter(List<DayTaskItem> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 从布局文件中创建视图
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item, parent, false);
            return new BillViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
            // 绑定数据到视图
            DayTaskItem task = tasks.get(position); // 获取当前的任务项
            holder.taskName.setText(task.getName()); // 设置任务名称
            holder.taskPoints.setText(String.valueOf(task.getAchievement_Points())); // 设置任务分数
            if (task.getDate() != null) { // 如果任务有日期，就显示日期
                holder.taskDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.getDate()));
            } else { // 如果任务没有日期，就显示未完成
                holder.taskDate.setText("未完成");
            }
        }

        @Override
        public int getItemCount() {
            // 返回任务列表的大小
            return tasks.size();
        }

        // 定义一个内部类，表示每个列表项的视图
        public class BillViewHolder extends RecyclerView.ViewHolder {
            TextView taskName; // 任务名称
            TextView taskPoints; // 任务分数
            TextView taskDate; // 任务日期

            public BillViewHolder(@NonNull View itemView) {
                super(itemView);
                taskName = itemView.findViewById(R.id.task_name);
                taskPoints = itemView.findViewById(R.id.task_points);
                taskDate = itemView.findViewById(R.id.task_date);
            }
        }

    }
    */




