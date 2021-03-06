package com.junsoft.perofu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.github.javiersantos.appupdater.AppUpdater;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.karacce.buttom.Buttom;

import java.io.File;

import spencerstudios.com.bungeelib.Bungee;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    VideoView mVideoView;

    Buttom scanBt;
    Buttom serverBt;
    Buttom localBt;
    TextView deviceName;
    ImageView back;

    private BluetoothDevice selectedDevice;

    int FILE_REQUEST_CODE = 200;
    private static final int SELECT_FILE_REQ = 1;
    private static final int SELECT_INIT_FILE_REQ = 2;
    private static final String EXTRA_URI = "uri";

    private String filePath;
    private Uri fileStreamUri;
    private String initFilePath;
    private Uri initFileStreamUri;
    private int fileType;
    private int fileTypeTmp;

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
        back = (ImageView)findViewById(R.id.back);

        scanBt = (Buttom) findViewById(R.id.scanClick);
        serverBt = (Buttom) findViewById(R.id.server);
        localBt = (Buttom) findViewById(R.id.local);

        deviceName = (TextView) findViewById(R.id.devicename);

    //    RelativeLayout main = (RelativeLayout) findViewById(R.id.movie);

        TextView first = (TextView)findViewById(R.id.firstT);
        TextView second = (TextView)findViewById(R.id.secondT);
        TextView third = (TextView)findViewById(R.id.thirdT);
        TextView deviceL = (TextView)findViewById(R.id.device_l);


     //   Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/demo");
        Uri uri = Uri.parse("https://palmcat.co.kr/sw/fumode-guide.mp4");
        mVideoView.setVideoURI(uri);

        Display display = getWindowManager().getDefaultDisplay();

        RelativeLayout firstT = (RelativeLayout)findViewById(R.id.first);

        RelativeLayout seconT = (RelativeLayout)findViewById(R.id.scan);

        RelativeLayout firmwareT = (RelativeLayout)findViewById(R.id.firmware);

        Point size = new Point();
        display.getSize(size);

       int mHeight = (int)(size.x * (9.0f / 16.0f));
        int height0 = (int)((size.y - convertDpToPixel(64))/3 );
        int height = (int)((size.y - height0 - convertDpToPixel(64))/2 );

     //   RelativeLayout.LayoutParams param0 = (RelativeLayout.LayoutParams) firstT.getLayoutParams();

        RelativeLayout.LayoutParams param0 = (RelativeLayout.LayoutParams) firstT.getLayoutParams();
        RelativeLayout.LayoutParams param1 = (RelativeLayout.LayoutParams) seconT.getLayoutParams();
        RelativeLayout.LayoutParams param2 = (RelativeLayout.LayoutParams) firmwareT.getLayoutParams();

        RelativeLayout.LayoutParams mparam0 = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();

        mparam0.width = (int) (16 * (height0 - convertDpToPixel(20)) * height / 9.0);
        param0.height = height0;
        param1.height = height;
        param2.height = height;

        int offset = (int)((size.y - (height + convertDpToPixel(64 + 26 + 11)) ) / 3.0f ) ;


        int secondDiff = (int)( height - convertDpToPixel(89 + 14));
        int thirdDiff = (int)( height - convertDpToPixel(118 ));

        // 44*2 + 10 + 20

        RelativeLayout.LayoutParams sParam0 = (RelativeLayout.LayoutParams) second.getLayoutParams();
        RelativeLayout.LayoutParams sParam1 = (RelativeLayout.LayoutParams) third.getLayoutParams();

        sParam0.topMargin = secondDiff / 2;
        sParam1.topMargin = thirdDiff / 2;


        int orgThirdHeight = (int) convertDpToPixel(118);

        float diff = height;

        diff = height - orgThirdHeight;
        float ratio = (float)(diff )/ (float)(orgThirdHeight);

        ratio = ((float)(height  - convertDpToPixel(40))/ (float)((orgThirdHeight + convertDpToPixel(40))));


        if(ratio >= 2)
        {
            ratio = 1.5f;

        }
        if(ratio < 0.8)
        {
            ratio = 0.8f;

        }
        scanBt.getLayoutParams().height = (int)convertDpToPixel(44 * ratio);

        serverBt.getLayoutParams().height = (int)convertDpToPixel(44 * ratio);
        localBt.getLayoutParams().height = (int)convertDpToPixel(44 * ratio);

        scanBt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,15 * ratio);
        serverBt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,15 * ratio);
        localBt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,15 * ratio);

        first.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,15 * ratio);
        second.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,15 * ratio);
        third.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,15 * ratio);

        deviceL.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,15 * ratio);
        deviceName.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,15 * ratio);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 준비 완료되면 비디오 재생
                back.setVisibility(View.GONE);

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
    public float convertPixelsToDp(float px){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }





    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final Uri uri = args.getParcelable(EXTRA_URI);
        /*
         * Some apps, f.e. Google Drive allow to select file that is not on the device. There is no "_data" column handled by that provider. Let's try to obtain
         * all columns and than check which columns are present.
         */
        // final String[] projection = new String[] { MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.DATA };
        return new CursorLoader(this, uri, null /* all columns, instead of projection */, null, null, null);
    }
    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
       /*
        fileNameView.setText(null);
        fileTypeView.setText(null);
        fileSizeView.setText(null);

        */
        filePath = null;
        fileStreamUri = null;
     //   statusOk = false;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        if (data != null && data.moveToNext()) {
            /*
             * Here we have to check the column indexes by name as we have requested for all. The order may be different.
             */
            final String fileName = data.getString(data.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)/* 0 DISPLAY_NAME */);
            final int fileSize = data.getInt(data.getColumnIndex(MediaStore.MediaColumns.SIZE) /* 1 SIZE */);
            String filePath = null;
            final int dataIndex = data.getColumnIndex(MediaStore.MediaColumns.DATA);
            if (dataIndex != -1)
                filePath = data.getString(dataIndex /* 2 DATA */);
            if (!TextUtils.isEmpty(filePath))
                this.filePath = filePath;

//            Log.d("",filePath);

            DeviceManager.getInstance().firmwareName =  fileName;
            Intent intent = new Intent(this, UpdateActivity.class);
            startActivity(intent);
            Bungee.fade(this);

            //  updateFileInfo(fileName, fileSize, fileType);
        } else {
            /*
            fileNameView.setText(null);
            fileTypeView.setText(null);
            fileSizeView.setText(null);

             */
            filePath = null;
            fileStreamUri = null;
            /*
            fileStatusView.setText(R.string.dfu_file_status_error);
            statusOk = false;

             */
        }
    }
    public float convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    void showAlert()
    {
        AlertDialog.Builder myAlertBuilder =
                new AlertDialog.Builder(MainActivity.this);
        // alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("Alert");
        myAlertBuilder.setMessage("Connect your PERO first!");
        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                // OK 버튼을 눌렸을 경우
          //      Toast.makeText(getApplicationContext(),"Pressed OK",
               //         Toast.LENGTH_SHORT).show();
            }
        });
        /*
        myAlertBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancle 버튼을 눌렸을 경우
                Toast.makeText(getApplicationContext(),"Pressed Cancle",
                        Toast.LENGTH_SHORT).show();
            }
        });

         */
        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
        myAlertBuilder.show();
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
/*
                if( DeviceManager.getInstance().selectedDevice != null)
                {
                    goServerList();

                }
                else
                {
                    showAlert();
                }

 */
                goServerList();

            }
        });
        localBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if( DeviceManager.getInstance().selectedDevice != null)
                {
                    goLocalList();

                }
                else
                {
                    showAlert();

                }
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

        new AppUpdater(this)
                .setTitleOnUpdateAvailable("Update")
                .setContentOnUpdateAvailable("There is a new version. Do you want to update?")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update Now")
                .setButtonDismiss(null)
                .setButtonDoNotShowAgain(null)
                // .setIcon(R.drawable.intro_logo) // Notification icon
                .setCancelable(false)
                .start();

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
        /*
        Intent intent = new Intent(this, LocalFirmwareActivity.class);
        startActivity(intent);
        Bungee.fade(this);

         */
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
       // intent.putExtra("toolbarColor", Color.WHITE);
        intent.setType( DfuService.MIME_TYPE_ZIP );
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // file browser has been found on the device
            startActivityForResult(intent, SELECT_FILE_REQ);
            Bungee.fade(this);

        } else {
            // there is no any file browser app, let's try to download one
            /*
            final View customView = getLayoutInflater().inflate(R.layout.app_file_browser, null);
            final ListView appsList = customView.findViewById(android.R.id.list);
            appsList.setAdapter(new FileBrowserAppsAdapter(this));
            appsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            appsList.setItemChecked(0, true);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dfu_alert_no_filebrowser_title)
                    .setView(customView)
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        final int pos = appsList.getCheckedItemPosition();
                        if (pos >= 0) {
                            final String query = getResources().getStringArray(R.array.dfu_app_file_browser_action)[pos];
                            final Intent storeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
                            startActivity(storeIntent);
                        }
                    })
                    .show();

             */
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case SELECT_FILE_REQ: {
                // clear previous data

                // and read new one
                final Uri uri = data.getData();
                /*
                 * The URI returned from application may be in 'file' or 'content' schema. 'File' schema allows us to create a File object and read details from if
                 * directly. Data from 'Content' schema must be read by Content Provider. To do that we are using a Loader.
                 */
                if (uri.getScheme().equals("file")) {
                    // the direct path to the file has been returned
                    final String path = uri.getPath();
                    final File file = new File(path);
             //       filePath = path;

               //     updateFileInfo(file.getName(), file.length(), fileType);
                } else if (uri.getScheme().equals("content")) {
                    // an Uri has been returned

                    fileStreamUri = uri;
                    // if application returned Uri for streaming, let's us it. Does it works?
                    // FIXME both Uris works with Google Drive app. Why both? What's the difference? How about other apps like DropBox?
                    final Bundle extras = data.getExtras();
                    if (extras != null && extras.containsKey(Intent.EXTRA_STREAM))
                        fileStreamUri = extras.getParcelable(Intent.EXTRA_STREAM);

                    final String path = uri.getPath();

                    // file name and size must be obtained from Content Provider
                    final Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_URI, uri);

                   DeviceManager.getInstance().fwUri = uri;



                        getLoaderManager().restartLoader(SELECT_FILE_REQ, bundle, this);




                }
                break;
            }
            case SELECT_INIT_FILE_REQ: {

                initFilePath = null;
                initFileStreamUri = null;

                // and read new one
                final Uri uri = data.getData();

                if (uri.getScheme().equals("file")) {
                    // the direct path to the file has been returned
                    initFilePath = uri.getPath();
                 //   fileStatusView.setText(R.string.dfu_file_status_ok_with_init);
                } else if (uri.getScheme().equals("content")) {
                    // an Uri has been returned
                    initFileStreamUri = uri;
                    // if application returned Uri for streaming, let's us it. Does it works?
                    // FIXME both Uris works with Google Drive app. Why both? What's the difference? How about other apps like DropBox?
                    final Bundle extras = data.getExtras();
                    if (extras != null && extras.containsKey(Intent.EXTRA_STREAM))
                        initFileStreamUri = extras.getParcelable(Intent.EXTRA_STREAM);
                   // fileStatusView.setText(R.string.dfu_file_status_ok_with_init);
                }


                break;
            }
            default:
                break;
        }
    }


}
