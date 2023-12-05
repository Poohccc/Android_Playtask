package com.jnu.student;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.jnu.student.data.TaskItem;
import com.jnu.student.data.DataBank;

import java.util.ArrayList;


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
    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private TaskItemsAdapter taskItemsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);

        //获取RecyclerView控件
        RecyclerView recycle_view_tasks = rootView.findViewById(R.id.recycle_view_tasks);
        //为线性布局
        recycle_view_tasks.setLayoutManager(new LinearLayoutManager(requireActivity()));
        taskItems = new DataBank().LoadTaskItems(requireActivity());
        if(taskItems.size()==0){
            taskItems.add(new TaskItem("普通阅读30分钟", 10));
            taskItems.add(new TaskItem("专业阅读30分钟", 15));
        }


        taskItemsAdapter = new TaskItemsAdapter(taskItems);
        recycle_view_tasks.setAdapter(taskItemsAdapter);

        registerForContextMenu(recycle_view_tasks);


        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        String pointText = data.getStringExtra("point");
                        double point=Double.parseDouble(pointText);
                        taskItems.add(new TaskItem(name, point));

                        taskItemsAdapter.notifyItemInserted(taskItems.size());
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
                        TaskItem taskItem = taskItems.get(position);
                        taskItem.setName(name);
                        taskItem.setAchievement_Points(point);
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
                new DataBank().SaveTaskItems(requireActivity(), taskItems);
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Delete Data");
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskItems.remove(item.getOrder());
                        taskItemsAdapter.notifyItemRemoved(item.getOrder());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                new DataBank().SaveTaskItems(requireActivity(), taskItems);
                break;

            case 2:
                Intent intentUpdate =new Intent(requireActivity(),BookItemDetailsActivity.class);

                TaskItem taskItem = taskItems.get(item.getOrder());
                intentUpdate.putExtra("name", taskItem.getName());
                intentUpdate.putExtra("point",taskItem.getAchievement_Points());
                intentUpdate.putExtra("position",item.getOrder());
                updateItemLauncher.launch(intentUpdate);
                new DataBank().SaveTaskItems(requireActivity(), taskItems);
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    //BookItemsAdapter.ViewHolder
    public class TaskItemsAdapter extends RecyclerView.Adapter<TaskItemsAdapter.ViewHolder>{

        private ArrayList<TaskItem> taskItemsArrayList;
        private boolean isEmpty;
        public TaskItemsAdapter(ArrayList<TaskItem> taskItems) {
            taskItemsArrayList = taskItems;
            isEmpty = taskItems.isEmpty();
        }



        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final TextView pointViewItem;

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
            //数据是否为空会显示不同布局
            //if (isEmpty) {
               // View emptyView = LayoutInflater.from(viewGroup.getContext())
                     //   .inflate(R.layout.empty_view, viewGroup, false);
              //  return new ViewHolder(emptyView);
           // }else{
              //  View view = LayoutInflater.from(viewGroup.getContext())
               //         .inflate(R.layout.activity_main2, viewGroup, false);
               // return new ViewHolder(view);
           // }
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_main2, viewGroup, false);

            return new ViewHolder(view);

        }



        @Override


        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            if (!isEmpty){
                viewHolder.getTextViewName().setText((taskItemsArrayList.get(position).getName()));
                viewHolder.getAchievement_PointsViewItem().setText((taskItemsArrayList.get(position).getAchievement_Points()+ ""));

            }




        }


        public int getItemCount() {
            return isEmpty ? 1 : taskItemsArrayList.size();
        }
    }


}