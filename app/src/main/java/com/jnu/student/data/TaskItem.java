package com.jnu.student.data;

import java.io.Serializable;

public class TaskItem implements Serializable {
    private String name;
    private double Achievement_Points;


    //private final int imageId;

    public TaskItem(String name_, double Achievement_Points_) {
        this.name=name_;
        this.Achievement_Points=Achievement_Points_;

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



    public void setName(String name){
        this.name=name;
    }

    public void setAchievement_Points(double Achievement_Points){
        this.Achievement_Points=Achievement_Points;
    }


}
