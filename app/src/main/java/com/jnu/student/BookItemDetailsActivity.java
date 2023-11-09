package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item_details);

        Button buttonOk=findViewById(R.id.button_item_detailsOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                EditText editTextItemName=findViewById(R.id.edittext_item_name);

                intent.putExtra("name",editTextItemName.getText().toString());
                setResult(Activity.RESULT_OK,intent);
                BookItemDetailsActivity.this.finish();
            }
        });

    }


}