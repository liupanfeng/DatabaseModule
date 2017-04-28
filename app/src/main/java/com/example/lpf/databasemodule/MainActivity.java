package com.example.lpf.databasemodule;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * 本工程用于总结数据库模块
 * 目的：下一个工程能够快速的使用数据库，建立高效的数据库。
 * 描述：前期主要总结使用contentProvider对数据库的操作
 */
public class MainActivity extends AppCompatActivity {

    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String xmlStr=XMLParseHelper.getXmlStr(this,"test.xml");
        Log.v("lpf","xmlStr:"+xmlStr);

    }
}
