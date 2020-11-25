package com.junsoft.perofu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.karacce.buttom.Buttom;

import spencerstudios.com.bungeelib.Bungee;


public class MainActivity extends AppCompatActivity {

    VideoView mVideoView;

    Buttom scanBt;
    Buttom serverBt;
    Buttom localBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //scanBt = findViewById(R.id.scan);


         mVideoView = (VideoView) findViewById(R.id.screenVideoView);

        scanBt = (Buttom) findViewById(R.id.scanClick);
        serverBt = (Buttom) findViewById(R.id.server);
        localBt = (Buttom) findViewById(R.id.local);


        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/demo");
        mVideoView.setVideoURI(uri);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 준비 완료되면 비디오 재생
                mp.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 준비 완료되면 비디오 재생
                mp.seekTo(0);
                mp.start();
            }
        });
        setBtListner();
    }

    void setBtListner()
    {
        scanBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                goScanList();
            }
        });
        serverBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                goServerList();
            }
        });
        localBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                goLocalList();
            }
        });
    }
    void goScanList()
    {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        Bungee.fade(this);

    }
    void goServerList()
    {
        Intent intent = new Intent(this, ServerFirmwareActivity.class);
        startActivity(intent);
        Bungee.fade(this);

    }
    void goLocalList()
    {
        Intent intent = new Intent(this, LocalFirmwareActivity.class);
        startActivity(intent);
        Bungee.fade(this);

    }
}
