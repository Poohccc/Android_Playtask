package com.jnu.student.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PointsViewModel extends ViewModel {
    // 使用LiveData封装totalPoints，这样可以在数据变化时通知观察者
    private MutableLiveData<Double> totalPoints;

    public PointsViewModel() {
        // 初始化totalPoints
        totalPoints = new MutableLiveData<>();
        totalPoints.setValue(0.0);
    }

    // 获取totalPoints的LiveData对象
    public LiveData<Double> getTotalPoints() {
        return totalPoints;
    }

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

