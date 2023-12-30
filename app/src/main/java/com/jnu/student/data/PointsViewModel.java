package com.jnu.student.data;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jnu.student.BillFragment;

import java.util.ArrayList;
import java.util.List;

public class PointsViewModel extends ViewModel {
    // 使用LiveData封装totalPoints，这样可以在数据变化时通知观察者
    private MutableLiveData<Double> totalPoints;


    private final ObservableArrayList< DayTaskItem>  billItems = new ObservableArrayList<>();
    public PointsViewModel() {
        // 初始化totalPoints
        totalPoints = new MutableLiveData<>();
        totalPoints.setValue(0.0);
    }

    public ObservableList<DayTaskItem> getBillItems() {
        return billItems;
    }

    // 获取totalPoints的LiveData对象
    public LiveData<Double> getTotalPoints() {

        return totalPoints;
    }



/*
    public void addTask(DayTaskItem task) { // 新增的方法，添加或更新任务项
        List<DayTaskItem> currentTasks = tasks.getValue(); // 获取当前的任务列表
        if (currentTasks == null) {
            currentTasks = new ArrayList<>();
        }
        int position = currentTasks.indexOf(task); // 查找任务项是否已经存在
        if (position == -1) { // 如果不存在，就添加到列表中
            currentTasks.add(task);
        } else { // 如果存在，就更新它的属性
            currentTasks.set(position, task);
        }
        tasks.setValue(currentTasks); // 更新LiveData的值
        //totalPoints.setValue(totalPoints.getValue() + task.getAchievement_Points()); // 更新总分数
    }

*/


    // 增加积分的方法
    public void addPoints(double points) {
        // 获取当前的积分值，然后加上要增加的积分，再设置回totalPoints
        double currentPoints = totalPoints.getValue();
        totalPoints.setValue(currentPoints + points);
    }

    // 减少积分的方法
    public void subtractPoints(double points) {
        // 获取当前的积分值，然后减去要减少的积分，再设置回totalPoints
        double currentPoints = totalPoints.getValue();
        totalPoints.setValue(currentPoints - points);
    }
}

