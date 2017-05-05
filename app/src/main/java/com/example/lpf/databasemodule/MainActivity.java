package com.example.lpf.databasemodule;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ImageView;

import com.example.lpf.databasemodule.xmlparse.XMLParseHelper;

/**
 * 本工程用于总结数据库模块
 * 目的：下一个工程能够快速的使用数据库，建立高效的数据库。
 * 描述：前期主要总结使用contentProvider对数据库的操作
 */
public class MainActivity extends Activity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a00002);
        String xmlStr= XMLParseHelper.getXmlStr(this,"test_delete_final.xml");
        Log.v("lpf","xmlStr:"+xmlStr);
        XMLParseHelper.parseXMLWithPull(xmlStr);


//        TextView
        LayoutInflater inflater=LayoutInflater.from(this);
        inflater.inflate(R.layout.a00002,null,false);

    }
}
