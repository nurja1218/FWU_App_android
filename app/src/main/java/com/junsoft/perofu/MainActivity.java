package com.junsoft.perofu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.karacce.buttom.Buttom;

import spencerstudios.com.bungeelib.Bungee;


public class MainActivity extends AppCompatActivity  {

    VideoView mVideoView;

    Buttom scanBt;
    Buttom serverBt;
    Buttom localBt;
    TextView deviceName;
    private BluetoothDevice selectedDevice;

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

        deviceName = (TextView) findViewById(R.id.devicename);


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
    /*
            SharedPreferences mPrefs = getSharedPreferences("FACE_EFFECT", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        prefsEditor.putFloat("FACE_NARROW_VAL", fNarrowValue );

        prefsEditor.commit();
     */
    @Override
    protected void onResume() {

        super.onResume();


        if(DeviceManager.getInstance().selectedDevice != null)
        {
            deviceName.setText(DeviceManager.getInstance().deviceName);
        }
     
    }

    void goScanList()
    {
        Intent intent = new Intent(this, ScanActivity.class);

     //   intent.p("PARENT", this);

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
