package com.junsoft.perofu;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import spencerstudios.com.bungeelib.Bungee;

public class ScanActivity extends Activity {

    RelativeLayout mainView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

*/
        setContentView(R.layout.activity_scan);


        mainView = (RelativeLayout)findViewById(R.id.main);


        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //gesture detector to detect swipe.

                finish();
                close();

                return true;//always return true to consume event

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
}