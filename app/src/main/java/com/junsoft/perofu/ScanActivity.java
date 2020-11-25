package com.junsoft.perofu;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class ScanActivity extends Activity {

    RelativeLayout mainView;
    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;

    public class CustomData {
        public String name;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);


        mainView = (RelativeLayout)findViewById(R.id.main);
        ListView listview = (ListView) findViewById(R.id.listview1) ;





        List<CustomData> objects = new ArrayList<CustomData>();

        for (int i = 0; i < 10; i++) {
            CustomData item = new CustomData();
            item.name = "PERO";
            objects.add(item);
        }

        CustomAdapter customAdapter = new CustomAdapter(this, 0, objects);

        listview.setAdapter(customAdapter);

        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //gesture detector to detect swipe.

                finish();
                close();

                return true;//always return true to consume event

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 항목에 해당하는 데이터 객체 얻기
                CustomData item = (CustomData) parent.getItemAtPosition(position);

                finish();
                close();


            }
        });


    }
    @Override
    protected void onResume() {

        super.onResume();
    }


    void close()
    {
        Bungee.fade(this);

    }
        @Override
    public void onBackPressed(){
        super.onBackPressed();



            Bungee.fade(this);


    }
    private class CustomAdapter extends ArrayAdapter<CustomData> {

        private LayoutInflater mLayoutInflater;

        public CustomAdapter(Context context, int resource, List<CustomData> objects) {
            super(context, resource, objects);
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 특정 행(position)의 데이터를 구함
            CustomData item = (CustomData) getItem(position);

            // 같은 행에 표시시킨 View는 재사용되기 때문에 첫 회만 생성
            if (null == convertView) {
                convertView = mLayoutInflater.inflate(R.layout.device_item, null);
            }

            TextView textView1 = (TextView) convertView.findViewById(R.id.name);
            textView1.setText(item.name);

            // 데이터를 View의 각 Widget에 설정
            /*
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            imageView.setImageBitmap(item.getImageData());


            TextView  textView2 = (TextView) convertView.findViewById(R.id.text2);
            textView2.setText(item.getText2Data());
            */


            return convertView;
        }
    }

}