package com.jnu.student.data;

public class BookItem {
    private String name;
    private final int imageId;

    public BookItem(String name_,int imageId_) {
        this.name=name_;
        this.imageId=imageId_;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }

    public void setName(String name){
        this.name=name;
    }

}
