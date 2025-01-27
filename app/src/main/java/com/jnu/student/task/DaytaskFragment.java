package com.jnu.student.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jnu.student.R;
import com.jnu.student.data.DayTaskItem;
import com.jnu.student.data.PointsViewModel;
import com.jnu.student.data.Data_day_Bank;
import com.jnu.student.main.MaintaskFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class DaytaskFragment extends Fragment {


    public DaytaskFragment() {
        // Required empty public constructor
    }

    public static DaytaskFragment newInstance() {
        DaytaskFragment fragment = new DaytaskFragment();
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
    private ArrayList<DayTaskItem> dayTaskItems = new ArrayList<>();
    private TaskItemsAdapter taskItemsAdapter;

    private TextView textViewTotalPoints;// 用来显示总的积分

    private double totalPoints; // 新增的属性，用来记录总的积分

    // 声明一个PointsViewModel的变量
    private PointsViewModel pointsViewModel;

    // 在你的BookListFragment中，声明一个HashMap来存储任务和分数
    HashMap<String, Integer> taskBill = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);

        //获取RecyclerView控件
        RecyclerView recycle_view_tasks = rootView.findViewById(R.id.recycle_view_tasks);
        //为线性布局
        recycle_view_tasks.setLayoutManager(new LinearLayoutManager(requireActivity()));
        // 获取TextView控件的引用
        textViewTotalPoints = rootView.findViewById(R.id.text_view_total_points);
        totalPoints = 0; // 初始化为0
        dayTaskItems = new Data_day_Bank().LoadTaskItems(requireActivity());
        if(dayTaskItems.size()==0){
            dayTaskItems.add(new DayTaskItem("普通阅读30分钟", 10));
            dayTaskItems.add(new DayTaskItem("专业阅读30分钟", 15));
        }


        taskItemsAdapter = new TaskItemsAdapter(dayTaskItems);
        recycle_view_tasks.setAdapter(taskItemsAdapter);

        registerForContextMenu(recycle_view_tasks);


        View btnAdd=rootView.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到添加任务活动
                Intent intent =new Intent(requireActivity(), BookItemDetailsActivity.class);
                addItemLauncher.launch(intent);
                new Data_day_Bank().SaveTaskItems(requireActivity(), dayTaskItems);

            }
        });


        // 从MaintaskFragment中获取PointsViewModel的实例
        pointsViewModel = ((MaintaskFragment) getParentFragment()).getPointsViewModel();

        // 观察totalPoints的变化，更新textViewTotalPoints的显示
        pointsViewModel.getTotalPoints().observe(getViewLifecycleOwner(), new Observer<Double>() {
            public void onChanged(Double points) {
                textViewTotalPoints.setText("Total points: " + points);
            }
        });


        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        String pointText = data.getStringExtra("point");
                        double point=Double.parseDouble(pointText);
                        dayTaskItems.add(new DayTaskItem(name, point));

                        taskItemsAdapter.notifyItemInserted(dayTaskItems.size());
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );

        updateItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int position= data.getIntExtra("position",0);
                        String name = data.getStringExtra("name");
                        String pointText = data.getStringExtra("point");
                        double point= Double.parseDouble(pointText);
                        DayTaskItem dayTaskItem = dayTaskItems.get(position);
                        dayTaskItem.setName(name);
                        dayTaskItem.setAchievement_Points(point);
                        taskItemsAdapter.notifyItemChanged(position);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );


        return rootView;
    }

    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent>  updateItemLauncher;
    ActivityResultLauncher<Intent>  updatePointLauncher;

    public boolean onContextItemSelected(MenuItem item){

        switch(item.getItemId()){
            case 0:
                Intent intent =new Intent(requireActivity(),BookItemDetailsActivity.class);
                addItemLauncher.launch(intent);
                new Data_day_Bank().SaveTaskItems(requireActivity(), dayTaskItems);
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Delete Data");
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DayTaskItem dayTaskItem = dayTaskItems.get(item.getOrder()); // 根据position获取要删除的任务
                        totalPoints -= dayTaskItem.getAchievement_Points();
                        pointsViewModel.subtractPoints(dayTaskItem.getAchievement_Points());
                        totalPoints= pointsViewModel.getTotalPoints().getValue();
                        textViewTotalPoints.setText("Total points: " + totalPoints); // 更新TextView控件的文本
                        dayTaskItems.remove(item.getOrder());

                        taskItemsAdapter.notifyItemRemoved(item.getOrder());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                new Data_day_Bank().SaveTaskItems(requireActivity(), dayTaskItems);
                break;

            case 2:
                Intent intentUpdate =new Intent(requireActivity(),BookItemDetailsActivity.class);

                DayTaskItem dayTaskItem = dayTaskItems.get(item.getOrder());
                intentUpdate.putExtra("name", dayTaskItem.getName());
                intentUpdate.putExtra("point", dayTaskItem.getAchievement_Points());
                intentUpdate.putExtra("position",item.getOrder());
                updateItemLauncher.launch(intentUpdate);
                new Data_day_Bank().SaveTaskItems(requireActivity(), dayTaskItems);
                break;


            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    //BookItemsAdapter.ViewHolder
    public class TaskItemsAdapter extends RecyclerView.Adapter<TaskItemsAdapter.ViewHolder>{

        private ArrayList<DayTaskItem> dayTaskItemsArrayList;

        private boolean isEmpty;

        public TaskItemsAdapter(ArrayList<DayTaskItem> dayTaskItems) {
            dayTaskItemsArrayList = dayTaskItems;

            isEmpty = dayTaskItems.isEmpty();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final TextView pointViewItem;
            private  CheckBox checkbox_task;
            public void  onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, 0, this.getAdapterPosition(), "添加" + this.getAdapterPosition());
                menu.add(0, 1, this.getAdapterPosition(), "删除" + this.getAdapterPosition());
                menu.add(0, 2, this.getAdapterPosition(), "修改" + this.getAdapterPosition());

            }

            public ViewHolder(View taskItemView) {
                super(taskItemView);
                textViewName = taskItemView.findViewById(R.id.text_view_task_title);
                pointViewItem = taskItemView.findViewById(R.id.text_view_point);
                checkbox_task=taskItemView.findViewById(R.id.switch_task);
                taskItemView.setOnCreateContextMenuListener(this);
            }

            public TextView getTextViewName() {
                return textViewName;
            }

            public TextView getAchievement_PointsViewItem() {
                return pointViewItem;
            }


        }

        public int getItemViewType(int position) {
            // 根据数据是否为空返回不同的类型
            return isEmpty ? 0 : 1;
        }
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.daytask, viewGroup, false);

            return new ViewHolder(view);

        }



        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            if (!isEmpty){
                viewHolder.getTextViewName().setText((dayTaskItemsArrayList.get(position).getName()));
                viewHolder.getAchievement_PointsViewItem().setText((dayTaskItemsArrayList.get(position).getAchievement_Points()+ ""));

            }

            DayTaskItem dayTaskItem = dayTaskItems.get(position);

            // 为每个任务项的CheckBox设置一个OnCheckedChangeListener，用来监听任务的勾选状态，并调用toggleCompleted方法来更新任务的完成状态。
            viewHolder.checkbox_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    dayTaskItem.setCompleted(isChecked); // 切换任务的完成状态

                    DayTaskItem billItem = new DayTaskItem(dayTaskItem.getName(), dayTaskItem.getAchievement_Points(), new Date(),new Date());
                    if (isChecked) {
                        totalPoints += dayTaskItem.getAchievement_Points(); // 如果任务完成，增加积分
                        pointsViewModel.addPoints(dayTaskItem.getAchievement_Points());

                        // Update shared ViewModel
                        pointsViewModel.getBillItems().add(billItem);


                    } else {
                        totalPoints -= dayTaskItem.getAchievement_Points(); // 如果任务取消，减少积分
                        pointsViewModel.subtractPoints(dayTaskItem.getAchievement_Points());

                        // Update shared ViewModel
                        pointsViewModel. getBillItems().remove(billItem);  // 要重写BillItem的equals方法才能使这个方法有效


                    }
                    // 在这里可以显示或更新总的积分，例如使用一个TextView来显示
                    totalPoints= pointsViewModel.getTotalPoints().getValue();
                    textViewTotalPoints.setText("Total points: " + totalPoints);
                    // 通知 BillFragment 关于变化

                }

            });



            // 在TaskItemsAdapter类中，为每个任务项的TextView设置一个TextWatcher，用来监听任务的积分变化，并更新任务的积分属性。
            viewHolder.pointViewItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        double point = Double.parseDouble(s.toString());
                        totalPoints -= dayTaskItem.getAchievement_Points();
                        dayTaskItem.setAchievement_Points(point); // 更新任务的积分属性
                        totalPoints += dayTaskItem.getAchievement_Points();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    totalPoints= pointsViewModel.getTotalPoints().getValue();
                    textViewTotalPoints.setText("Total points: " + totalPoints);
                }
            });

        }

        public int getItemCount() {
            return isEmpty ? 1 : dayTaskItemsArrayList.size();
        }
    }


}