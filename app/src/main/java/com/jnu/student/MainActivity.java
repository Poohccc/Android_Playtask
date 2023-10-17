package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);  //设置布局中的控件居中显示
        TextView textView = new TextView(this);                       //创建TextView控件
        textView.setId(R.id.textView1);
        relativeLayout.addView(textView, params);                  //添加TextView对象和TextView的布局属性
        setContentView(relativeLayout);                                  //设置在Activity中显示RelativeLayout

        TextView helloWorldTextView = findViewById(R.id.textView1);
        helloWorldTextView.setText(getString(R.string.hello_android));
    }


}