package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookItemDetailsActivity extends AppCompatActivity {
    int position=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_item_details);


        Intent intent = getIntent();
        if (intent != null) {
            // 从Intent中获取传递的数据
            String name = intent.getStringExtra("name");
            if (null != name) {
                Double point =  intent.getDoubleExtra("point",0);
                position = intent.getIntExtra("position",-1);
                EditText editTextItemName= findViewById(R.id.edittext_item_name);
                editTextItemName.setText(name);
                EditText editTextItemPoint= findViewById(R.id.edittext_item_point);

                editTextItemPoint.setText(point.toString());

            }
        }


        Button buttonOk=findViewById(R.id.button_item_detailsOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                EditText editTextItemName=findViewById(R.id.edittext_item_name);
                EditText editTextItemPoint= findViewById(R.id.edittext_item_point);
                intent.putExtra("name",editTextItemName.getText().toString());
                intent.putExtra("point", editTextItemPoint.getText().toString());
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK,intent);
                BookItemDetailsActivity.this.finish();

            }
        });

    }


}

