package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button =findViewById(R.id.click_me_button);
        button.setOnClickListener(new Click());

    }

    private class Click implements View.OnClickListener {
        public void onClick(View v){
            Button clickedButton =(Button) v;



            TextView view1 =findViewById(R.id.textView1);
            TextView view2 =findViewById(R.id.textView2);
            String temp=view1.getText().toString();
            view1.setText(view2.getText().toString());
            view2.setText(temp);
        }
    }
}