package com.jnu.student;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取RecyclerView控件
        RecyclerView recycle_view_books = findViewById(R.id.recycle_view_books);
        //为线性布局
        recycle_view_books.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<BookItem> bookItems = new ArrayList<>();
        bookItems.add(new BookItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
        bookItems.add(new BookItem("创新工程实践", R.drawable.book_no_name));
        bookItems.add(new BookItem("信息安全数学基础（第2版）", R.drawable.book_1));

        BookItemsAdapter bookItemsAdapter = new BookItemsAdapter(bookItems);
        recycle_view_books.setAdapter(bookItemsAdapter);

        registerForContextMenu(recycle_view_books);
        ActivityResultLauncher<Intent> addItemLauncher;
        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        bookItems.add(new BookItem(name, R.drawable.book_2));
                        bookItemsAdapter.notifyItemRangeInserted(bookItems.size());
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );


    }
    ActivityResultLauncher<Intent> addItemLauncher;

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Toast.makeText(this,"clicked"+item.getOrder(),Toast.LENGTH_LONG).show();
        switch(item.getItemId()){
            case 0:



                Intent intent =new Intent(MainActivity.this,BookItemDetailsActivity.class);

                launcher.launch(intent);
                break;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

