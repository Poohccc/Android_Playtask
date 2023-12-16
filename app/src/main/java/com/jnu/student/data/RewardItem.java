package com.jnu.student.data;

import java.io.Serializable;

public class RewardItem implements Serializable {
    private String name;
    private double Points;
    private boolean completed;



    public RewardItem(String name_, double Points_) {
        this.name=name_;
        this.Points=Points_;
        this.completed = false;
    }

    public String getName(){

        return name;
    }
    public double getAchievement_Points(){
        return Points;
    }


    public boolean isCompleted() {
        return completed;
    }



    public void setName(String name){
        this.name=name;
    }

    public void setAchievement_Points(double Points){
        this.Points=Points;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
