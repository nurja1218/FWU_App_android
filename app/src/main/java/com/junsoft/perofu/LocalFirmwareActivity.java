package com.junsoft.perofu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import spencerstudios.com.bungeelib.Bungee;

public class LocalFirmwareActivity extends Activity {

    RelativeLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_firmware);
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