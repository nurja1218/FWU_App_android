package com.junsoft.perofu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.karacce.buttom.Buttom;

import java.io.File;
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
                starter.setZip( path);
                starter.start(this, DfuService.class);

            }

        }



    }
}