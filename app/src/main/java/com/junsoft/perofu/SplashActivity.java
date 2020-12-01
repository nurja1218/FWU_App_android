package com.junsoft.perofu;


import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import spencerstudios.com.bungeelib.Bungee;


public class SplashActivity extends Activity {

    Handler mTimerDisp;
    int timerCnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        RelativeLayout imgView = (RelativeLayout)findViewById(R.id.title);

        Display display = getWindowManager().getDefaultDisplay();


        Point size = new Point();
        display.getSize(size);

     //   imgView.getLayoutParams().width = (int)(0.6 *size.x) ;
        imgView.getLayoutParams().height = (int)(0.5*size.x) ;


        mTimerDisp = new Handler()
        {
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                timerCnt++;
                if( timerCnt < 2)
                {

                    this.sendEmptyMessageDelayed(0, 1000);        // REPEAT_DELAY 간격으로 계속해서 반복하게 만들어준다

                }
                else
                {

                    go();
                }




            }
        };
        mTimerDisp.sendEmptyMessage(0);
/*
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


        finish();
  */
    }
    void go()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


        finish();
        Bungee.fade(this);

    }
}
