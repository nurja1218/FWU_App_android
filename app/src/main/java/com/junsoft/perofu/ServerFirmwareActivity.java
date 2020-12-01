package com.junsoft.perofu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class ServerFirmwareActivity extends Activity {

    RelativeLayout mainView;
    ListView listview;
    public class CustomData {
        public String name;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_firmware);

        AndroidNetworking.initialize(getApplicationContext());

        mainView = (RelativeLayout)findViewById(R.id.main);
        listview = (ListView) findViewById(R.id.listview1) ;

        PRDownloader.initialize(getApplicationContext());

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 항목에 해당하는 데이터 객체 얻기
                CustomData item = (CustomData) parent.getItemAtPosition(position);

                DeviceManager.getInstance().firmwareName = item.name;


                download();


            }
        });

        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //gesture detector to detect swipe.

                finish();
                close();

                return true;//always return true to consume event

            }
        });
        getList();
    }
    void download()
    {
        String filename = DeviceManager.getInstance().firmwareName;
        String url = "http://www.junsoft.org/firmware/" + filename;

        String dirPath = Utils.getRootDirPath(getApplicationContext());


        int downloadId = PRDownloader.download(url, dirPath, filename)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        goUpdate();
                        finish();
                        close();
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
    }

    List<ServerFirmwareActivity.CustomData> objects = new ArrayList<ServerFirmwareActivity.CustomData>();

    void goUpdate()
    {
        Intent intent = new Intent(this, UpdateActivity.class);
        startActivity(intent);
        Bungee.fade(this);

    }

    void getList()
    {

        AndroidNetworking.get("http://www.junsoft.org/firmware/firmwares.json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject json) {
                        // do anything with response

                        Log.d("","");
                        try{
                            JSONArray items = json.getJSONArray("item");


                        //    JSONArray item = items.getJSONArray(0);

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject firmware = items.getJSONObject(i);
                                String name = firmware.get("name").toString();
                                Log.d("Server",name);
                                ServerFirmwareActivity.CustomData item = new ServerFirmwareActivity.CustomData();
                                item.name = name;
                                objects.add(item);


                            }
                            setAdapter();



                        }
                        catch (JSONException e)
                        {
                            Log.d("","");

                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("","");
                    }

                });
    }

    void setAdapter()
    {
        ServerFirmwareActivity.CustomAdapter customAdapter = new ServerFirmwareActivity.CustomAdapter(this, 0, objects);

        listview.setAdapter(customAdapter);

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
    private class CustomAdapter extends ArrayAdapter<ServerFirmwareActivity.CustomData> {

        private LayoutInflater mLayoutInflater;

        public CustomAdapter(Context context, int resource, List<ServerFirmwareActivity.CustomData> objects) {
            super(context, resource, objects);
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 특정 행(position)의 데이터를 구함
            ServerFirmwareActivity.CustomData item = (ServerFirmwareActivity.CustomData) getItem(position);

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