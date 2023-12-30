package com.jnu.student.data;

import android.icu.text.SimpleDateFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class DayTaskItem implements Serializable {
    private String name;
    private double Achievement_Points;
    private boolean completed;

    private Date date; // 新增的属性，记录任务项被勾选的日期
    private Date time; // 新增的属性，记录任务项被勾选的日期


    public DayTaskItem(String name_, double Achievement_Points_) {
        this.name=name_;
        this.Achievement_Points=Achievement_Points_;
        this.completed = false;
        this.date = null; // 初始化为null，表示未被勾选
        this.time = null; // 初始化为null，表示未被勾选
    }


    public DayTaskItem(String name, double achievement_Points, Date date,Date time) { // 新增的构造器，带有日期参数
        this.name = name;
        this.Achievement_Points = achievement_Points;
        this.date = date;
        this.time= time;
    }



    public String getName(){

        return name;
    }
    public double getAchievement_Points(){
        return Achievement_Points;
    }


    public Date getDate() { // 新增的方法，返回日期
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
        // 返回新的Date对象，它只包含年月日
        return newDate;
    }

    public Date getTime() { // 新增的方法，返回时间

        return time;
    }

    public boolean isCompleted() {
        return completed;
    }
    // 重写equals方法
    @Override
    public boolean equals(Object obj) {
        // 判断传入的对象是否是当前对象本身
        if (obj == this) {
            return true;
        }
        // 判断传入的对象是否是null
        if (obj == null) {
            return false;
        }
        // 判断传入的对象是否是BillItem类型的
        if (obj instanceof DayTaskItem) {
            // 将传入的对象强制转换成BillItem类型的
            DayTaskItem other = (DayTaskItem) obj;
            // 比较它们的属性值是否相等
            return this.name.equals(other.name) // 比较任务名称
                    && this.Achievement_Points == other.Achievement_Points; // 比较积分

        }
        // 如果没有走类型判断语句，说明传入的对象和当前对象类型不一样，返回false
        return false;
    }


    public void setDate(Date date) { // 新增的方法，设置日期
        this.date = date;
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

/*
    // 重写equals方法，用来判断两个任务项是否相同，只比较名称，不比较分数和日期
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayTaskItem that = (DayTaskItem) o;
        return Objects.equals(name, that.name);
    }

    // 重写hashCode方法，与equals方法保持一致
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
*/
}
