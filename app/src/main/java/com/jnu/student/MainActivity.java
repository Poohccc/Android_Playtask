package com.jnu.student;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.student.data.BookItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<BookItem> bookItems = new ArrayList<>();
    private BookItemsAdapter bookItemsAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取RecyclerView控件
        RecyclerView recycle_view_books = findViewById(R.id.recycle_view_books);
        //为线性布局
        recycle_view_books.setLayoutManager(new LinearLayoutManager(this));
        bookItems.add(new BookItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
        bookItems.add(new BookItem("创新工程实践", R.drawable.book_no_name));


        bookItemsAdapter = new BookItemsAdapter(bookItems);
        recycle_view_books.setAdapter(bookItemsAdapter);

        registerForContextMenu(recycle_view_books);


        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        bookItems.add(new BookItem(name, R.drawable.book_2));
                        bookItemsAdapter.notifyItemInserted(bookItems.size());
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );


    }
    ActivityResultLauncher<Intent> addItemLauncher;

    public boolean onContextItemSelected(MenuItem item){

        switch(item.getItemId()){
            case 0:
                Intent intent =new Intent(MainActivity.this,BookItemDetailsActivity.class);
                addItemLauncher.launch(intent);
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Data");
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookItems.remove(item.getOrder());
                        bookItemsAdapter.notifyItemRemoved(item.getOrder());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                break;

            case 2:
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    //BookItemsAdapter.ViewHolder
    public class BookItemsAdapter extends RecyclerView.Adapter<BookItemsAdapter.ViewHolder>{

        private ArrayList<BookItem> bookItemsArrayList;
        private boolean isEmpty;
        public BookItemsAdapter(ArrayList<BookItem> bookItems) {
            bookItemsArrayList= bookItems;
            isEmpty = bookItems.isEmpty();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final ImageView imageViewItem;

            public void  onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, 0, this.getAdapterPosition(), "添加" + this.getAdapterPosition());
                menu.add(0, 1, this.getAdapterPosition(), "删除" + this.getAdapterPosition());
                menu.add(0, 2, this.getAdapterPosition(), "修改" + this.getAdapterPosition());

            }

            public ViewHolder(View bookItemView) {
                super(bookItemView);
                textViewName = bookItemView.findViewById(R.id.text_view_book_title);
                imageViewItem = bookItemView.findViewById(R.id.image_view_book_cover);
                bookItemView.setOnCreateContextMenuListener(this);


            }

            public TextView getTextViewName() {
                return textViewName;
            }

            public ImageView getImageViewItem() {
                return imageViewItem;
            }

        }

        public int getItemViewType(int position) {
            // 根据数据是否为空返回不同的类型
            return isEmpty ? 0 : 1;
        }
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //数据是否为空会显示不同布局
            if (isEmpty) {
                View emptyView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.empty_view, viewGroup, false);
                return new ViewHolder(emptyView);
            }else{
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.activity_main2, viewGroup, false);
                return new ViewHolder(view);
            }


        }

        @Override


        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            if (!isEmpty){
                viewHolder.getTextViewName().setText((bookItemsArrayList.get(position).getName()));
                viewHolder.getImageViewItem().setImageResource((bookItemsArrayList.get(position).getImageId()));
            }

        }


        public int getItemCount() {
            return isEmpty ? 1 :bookItemsArrayList.size();
        }
    }


}





