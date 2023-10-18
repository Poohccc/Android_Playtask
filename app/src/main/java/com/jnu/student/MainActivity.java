package com.jnu.student;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取RecyclerView控件
        RecyclerView recycle_view_books=findViewById(R.id.recycle_view_books);
        //为线性布局
        recycle_view_books.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<BookItem> bookItems=new ArrayList<>();
        bookItems.add(new BookItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
        bookItems.add(new BookItem("创新工程实践", R.drawable.book_no_name));
        bookItems.add(new BookItem("信息安全数学基础（第2版）", R.drawable.book_1));

        BookItemsAdapter bookItemsAdapter=new BookItemsAdapter(bookItems);
        recycle_view_books.setAdapter(bookItemsAdapter);

    }


}