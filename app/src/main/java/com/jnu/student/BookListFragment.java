package com.jnu.student;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jnu.student.data.BookItem;
import com.jnu.student.data.DataBank;

import java.util.ArrayList;


public class BookListFragment extends Fragment {


    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance() {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    private ArrayList<BookItem> bookItems = new ArrayList<>();
    private BookItemsAdapter bookItemsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);

        //获取RecyclerView控件
        RecyclerView recycle_view_books = rootView.findViewById(R.id.recycle_view_books);
        //为线性布局
        recycle_view_books.setLayoutManager(new LinearLayoutManager(requireActivity()));
        bookItems = new DataBank().LoadBookItems(requireActivity());
        if(bookItems.size()==0){
            bookItems.add(new BookItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
            bookItems.add(new BookItem("创新工程实践", R.drawable.book_no_name));
        }


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

        updateItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int position= data.getIntExtra("position",0);
                        String name = data.getStringExtra("name");
                        BookItem bookItem=bookItems.get(position);
                        bookItem.setName(name);
                        bookItemsAdapter.notifyItemChanged(position);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );



        return rootView;
    }



    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent>  updateItemLauncher;

    public boolean onContextItemSelected(MenuItem item){

        switch(item.getItemId()){
            case 0:
                Intent intent =new Intent(requireActivity(),BookItemDetailsActivity.class);
                addItemLauncher.launch(intent);
                new DataBank().SaveBookItems(requireActivity(), bookItems);
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
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
                new DataBank().SaveBookItems(requireActivity(), bookItems);
                break;

            case 2:
                Intent intentUpdate =new Intent(requireActivity(),BookItemDetailsActivity.class);

                BookItem bookItem= bookItems.get(item.getOrder());
                intentUpdate.putExtra("name",bookItem.getName());
                intentUpdate.putExtra("position",item.getOrder());
                updateItemLauncher.launch(intentUpdate);
                new DataBank().SaveBookItems(requireActivity(), bookItems);
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