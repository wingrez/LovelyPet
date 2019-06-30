package com.wingrez.lovelypet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BltActivity extends AppCompatActivity {

    private Button scan;
    private Button search;
    private TextView localblumessage;
    private TextView bluemessage;
    private TextView scanfinnish;
    private ListView listview;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blt_main);
        initView();
    }

    public void initView(){
        scan=findViewById(R.id.scan);
        search=findViewById(R.id.sousuo);
        localblumessage=findViewById(R.id.localblumessage);
        bluemessage=findViewById(R.id.bluemessage);
        scanfinnish=findViewById(R.id.scanfinnish);
        listview=findViewById(R.id.listview);
    }



}
