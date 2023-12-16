package com.jnu.student.data;

import java.io.Serializable;

public class DayTaskItem implements Serializable {
    private String name;
    private double Achievement_Points;
    private boolean completed;

    //private final int imageId;

    public DayTaskItem(String name_, double Achievement_Points_) {
        this.name=name_;
        this.Achievement_Points=Achievement_Points_;
        this.completed = false;
    }

    public String getName(){

        return name;
    }
    public double getAchievement_Points(){
        return Achievement_Points;
    }

    //public int getImageId(){
     //   return imageId;
    //}

    public boolean isCompleted() {
        return completed;
    }


    public void toggleCompleted() {
        completed = !completed;
        if (completed) {
            //增加积分的逻辑

        } else {
            //减少积分的逻辑
        }
    }

    public void setName(String name){
        this.name=name;
    }

    public void setAchievement_Points(double Achievement_Points){
        this.Achievement_Points=Achievement_Points;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
