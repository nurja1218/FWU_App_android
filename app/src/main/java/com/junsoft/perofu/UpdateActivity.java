package com.junsoft.perofu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karacce.buttom.Buttom;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;

import at.grabner.circleprogress.CircleProgressView;
import no.nordicsemi.android.dfu.DfuBaseService;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;
import spencerstudios.com.bungeelib.Bungee;

import static android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT;


public class UpdateActivity extends Activity {

    CircleProgressView mCircleView;
    Boolean mShowUnit = true;
    private int fileType;
    int numberOfPackets = 0;
    private BluetoothDevice selectedDevice;
    String filePath;

    TextView percentView;
    TextView deviceName;
    TextView firmwareName;
    TextView status;

    Buttom updateBt;
    ImageView backBt;
    Boolean completed = false;
    Boolean progressing = false;


    private final DfuProgressListener dfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(@NonNull final String deviceAddress) {
          //  progressBar.setIndeterminate(true);
          //  textPercentage.setText(R.string.dfu_status_connecting);
        }

        @Override
        public void onDfuProcessStarting(@NonNull final String deviceAddress) {
          //  progressBar.setIndeterminate(true);
          //  textPercentage.setText(R.string.dfu_status_starting);
        }

        @Override
        public void onEnablingDfuMode(@NonNull final String deviceAddress) {
          //  progressBar.setIndeterminate(true);
           // textPercentage.setText(R.string.dfu_status_switching_to_dfu);
        }

        @Override
        public void onFirmwareValidating(@NonNull final String deviceAddress) {
          //  progressBar.setIndeterminate(true);
          //  textPercentage.setText(R.string.dfu_status_validating);
        }

        @Override
        public void onDeviceDisconnecting(@NonNull final String deviceAddress) {
          //  progressBar.setIndeterminate(true);
           // textPercentage.setText(R.string.dfu_status_disconnecting);
        }

        @Override
        public void onDfuCompleted(@NonNull final String deviceAddress) {
           // textPercentage.setText(R.string.dfu_status_completed);
            /*
            if (resumed) {
                // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
                new Handler().postDelayed(() -> {
                    onTransferCompleted();

                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }, 200);
            } else {
                // Save that the DFU process has finished
                dfuCompleted = true;
            }

             */

            status.setText("Completed");
            completed = true;
            progressing = false;
            backBt.setVisibility(View.VISIBLE);
            updateBt.setText("       Done");

            int[] colors = {Color.parseColor("#e8963d"),Color.parseColor("#c1315a")};

            GradientDrawable gradient = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, colors);
            gradient.setGradientType(LINEAR_GRADIENT);

          //  gradient.setColor(Color.parseColor("#b3b3b3"));
            gradient.setCornerRadius(convertDpToPixel(22.0f));

            updateBt.setBackground(gradient);

            DeviceManager.getInstance().firmwareName = "";
            DeviceManager.getInstance().fwUri = null;

        }

        @Override
        public void onDfuAborted(@NonNull final String deviceAddress) {
      //      textPercentage.setText(R.string.dfu_status_aborted);
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler().postDelayed(() -> {
              //  onUploadCanceled();

                // if this activity is still open and upload process was completed, cancel the notification
            //    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
              //  manager.cancel(DfuService.NOTIFICATION_ID);
            }, 200);
        }

        @Override
        public void onProgressChanged(@NonNull final String deviceAddress, final int percent,
                                      final float speed, final float avgSpeed,
                                      final int currentPart, final int partsTotal) {

            status.setText("Updating...");
            mCircleView.setValue(percent);

            percentView.setText(percent + "%");
            progressing = true;
         //   progressBar.setIndeterminate(false);
        //    progressBar.setProgress(percent);
         //   textPercentage.setText(getString(R.string.dfu_uploading_percentage, percent));
          //  if (partsTotal > 1)
           //     textUploading.setText(getString(R.string.dfu_status_uploading_part, currentPart, partsTotal));
           // else
            //    textUploading.setText(R.string.dfu_status_uploading);
        }

        @Override
        public void onError(@NonNull final String deviceAddress, final int error, final int errorType, final String message) {
           /*
            if (resumed) {
                showErrorMessage(message);

                // We have to wait a bit before canceling notification. This is called before DfuService creates the last notification.
                new Handler().postDelayed(() -> {
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }, 200);
            } else {
                dfuError = message;
            }

            */
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mCircleView = (CircleProgressView)findViewById(R.id.circleView);

        percentView = (TextView)findViewById(R.id.percent_label);

        deviceName = (TextView)findViewById(R.id.dev_name);
        firmwareName = (TextView)findViewById(R.id.fw_name);

        firmwareName = (TextView)findViewById(R.id.fw_name);

        status = (TextView)findViewById(R.id.status);

        updateBt = (Buttom)findViewById(R.id.update);

        backBt = (ImageView)findViewById(R.id.back);

        selectedDevice = DeviceManager.getInstance().selectedDevice;

        if(selectedDevice != null)
            deviceName.setText(selectedDevice.getName());

        firmwareName.setText(DeviceManager.getInstance().firmwareName);

        status.setText("Waiting");

        Display display = getWindowManager().getDefaultDisplay();


        Point size = new Point();
        display.getSize(size);

        mCircleView.setBarWidth(24);

        mCircleView.setRimWidth(23);

        float ratio = 1.0f;//(float)size.x / 800.0f;
        if(size.y == 800)
        {
            // 480 x 800
            mCircleView.setBarWidth((int)convertDpToPixel(21) );
            mCircleView.setRimWidth((int)convertDpToPixel(20) );

        }
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dpi =(float)metrics.densityDpi /  DisplayMetrics.DENSITY_DEFAULT;



        // xxhdpi : 96px
        // xhdpi :
        Class mipmap = R.mipmap.class;

        try {
            Field field = mipmap.getField("fback");
            Log.d("","");
            //int resId = field.get.getInt(imageName);

        } catch (NoSuchFieldException e) {//If the imageName is not found under "mipmap", it will return 0

        }

        int dp = (int)(96.0f / dpi);

        ImageView back = (ImageView)findViewById(R.id.progress_back);
        RelativeLayout l = (RelativeLayout)findViewById(R.id.percent_l);
        RelativeLayout l1 = (RelativeLayout)findViewById(R.id.progress_info);



        float offset = 64 + 28 +  1 + 100 + 10 + 1 + 44 + 10 + 20 + 24 ;//64 + 58 + 1 + 100 + 10 + 30 + 1 + 44;
        float height = size.y - convertDpToPixel(offset + 44 * 2);
        mCircleView.getLayoutParams().height = (int)height;
        back.getLayoutParams().height = (int)height;
        l.getLayoutParams().height = (int)height;
        l1.getLayoutParams().height = (int)height;

        float ratio0 = height / convertDpToPixel(277);

        mCircleView.setBarWidth((int)Math.ceil(24 * dpi * ratio0));
        mCircleView.setRimWidth((int)Math.ceil(23 * dpi* ratio0));

        if(dpi == 1.5 )
        {
         //   dpi = 0.75f;
            /*
            ImageView back = (ImageView)findViewById(R.id.progress_back);
            RelativeLayout l = (RelativeLayout)findViewById(R.id.percent_l);


            float offset = 64 + 58 + 1 + 100 + 10 + 30 + 1 + 44;
            float height = size.y - convertDpToPixel(offset + 44 * 2);
            mCircleView.getLayoutParams().height = (int)height;
            back.getLayoutParams().height = (int)height;
            l.getLayoutParams().height = (int)height;


             */
            TextView percent = (TextView)findViewById(R.id.percent_label);
            percent.setTextSize(42);

            updateBt.getLayoutParams().height = (int)convertDpToPixel(34);

            updateBt.setTextSize((int)(10));

        }
        else if( size.y<= 480)
        {
            RelativeLayout top = (RelativeLayout)findViewById(R.id.top);
            top.getLayoutParams().height = 48;

            ImageView top_img = (ImageView)findViewById(R.id.top_img);
            top_img.getLayoutParams().height = 22;

            ImageView back0 = (ImageView)findViewById(R.id.back);
            back0.getLayoutParams().height = 48;
            //

            TextView percent = (TextView)findViewById(R.id.percent_label);
            percent.setTextSize(18);

            updateBt.getLayoutParams().height = (int)convertDpToPixel(34);

            updateBt.setTextSize((int)(10));

            ImageView back2 = (ImageView)findViewById(R.id.progress_back);
            RelativeLayout l2 = (RelativeLayout)findViewById(R.id.percent_l);
            RelativeLayout l3 = (RelativeLayout)findViewById(R.id.progress_info);



            float offset0 = 48 + 28 +  1 + 100 + 10 + 1 + 44 + 10 + 20 + 24 ;//64 + 58 + 1 + 100 + 10 + 30 + 1 + 44;
            float height0 = size.y - convertDpToPixel(offset0 + 44 * 2);
            mCircleView.getLayoutParams().height = (int)height0;
            back2.getLayoutParams().height = (int)height0;
            l2.getLayoutParams().height = (int)height0;
            l3.getLayoutParams().height = (int)height0;

            float ratio1 = height0 / convertDpToPixel(277);

            mCircleView.setBarWidth((int)Math.ceil(24 * dpi * ratio1));
            mCircleView.setRimWidth((int)Math.ceil(23 * dpi* ratio1));

        }



        mCircleView.setValue(65);

        // fileType = DfuService.TYPE_AUTO; // Default


        updateBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                update();
            }
        });
        backBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                close();
            }
        });

        DfuServiceListenerHelper.registerProgressListener(this, dfuProgressListener);

    }
    void close()
    {
        finish();
        Bungee.fade(this);

    }
    @Override
    public void onBackPressed(){


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
     //   public String firmwareName;
     //   public Uri fwUri;
        DeviceManager.getInstance().firmwareName = "";
        DeviceManager.getInstance().fwUri = null;


        DfuServiceListenerHelper.unregisterProgressListener(this, dfuProgressListener);
    }
    public float convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }



    void update()
    {
        if(completed == true)
        {
            backBt.setVisibility(View.VISIBLE);
            close();

        }
        else
        {
            if(selectedDevice != null && (completed == false && progressing == false))
            {

              //  updateBt.setCo
                updateBt.setText("       Updating...");
              //  updateBt.buttom_corner_radius
//                Drawable d = (Drawable) updateBt.getBackground();

                //      d.setColor(Color.parseColor("#b3b3b3"));

                GradientDrawable gradient = new GradientDrawable();
                gradient.setShape(GradientDrawable.RECTANGLE);
                gradient.setColor(Color.parseColor("#b3b3b3"));
                gradient.setCornerRadius(convertDpToPixel(22.0f));


               updateBt.setBackgroundDrawable(gradient);

         //       updateBt.setBackgroundColor(Color.parseColor("#b3b3b3"));
                progressing = true;
                backBt.setVisibility(View.GONE);
                numberOfPackets = DfuServiceInitiator.DEFAULT_PRN_VALUE;
                String filename = DeviceManager.getInstance().firmwareName;

                filePath =  Utils.getRootDirPath(getApplicationContext()) +"/"+filename;

                //    let colors = [UIColor(hex: "b3b3b3"), UIColor(hex: "b3b3b3")]



                final DfuServiceInitiator starter = new DfuServiceInitiator(selectedDevice.getAddress())
                        .setDeviceName(selectedDevice.getName())
                        .setKeepBond(false)
                        .setForceDfu(false)
                        .setForeground(false)
                        .setDisableNotification(true)
                        //     .setPacketsReceiptNotificationsValue(DfuServiceInitiator.DEFAULT_PRN_VALUE)
                        .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true)
                        .setPrepareDataObjectDelay(300L);

                //  starter.setZip(fileStreamUri, filePath);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Context ctx = getApplicationContext();
                    DfuServiceInitiator.createDfuNotificationChannel(ctx);
                }

                Uri uri = Uri.parse(filePath);
                File file = new File(filePath);
                String path = file.getPath();

                if( DeviceManager.getInstance().fwUri != null)
                {
                    starter.setZip( DeviceManager.getInstance().fwUri);
                    DeviceManager.getInstance().fwUri = null;


                }
                else
                {
                    starter.setZip( path);

                }
                starter.start(this, DfuService.class);

            }

        }



    }
}