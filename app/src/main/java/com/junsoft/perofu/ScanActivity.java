package com.junsoft.perofu;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;
import spencerstudios.com.bungeelib.Bungee;

public class ScanActivity extends Activity {

    RelativeLayout mainView;
    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34; // any 8-bit number
    private final static String PARAM_UUID = "param_uuid";
    private final static long SCAN_DURATION = 5000;

    private BluetoothAdapter bluetoothAdapter;
    private OnDeviceSelectedListener listener;
    private DeviceListAdapter adapter;
    private final Handler handler = new Handler();
    private Button scanButton;

    private View permissionRationale;

    private ParcelUuid uuid;

    private boolean scanning = false;
    public class CustomData {
        public String name;

    }
    public interface OnDeviceSelectedListener {
        /**
         * Fired when user selected the device.
         *
         * @param device
         *            the device to connect to
         * @param name
         *            the device name. Unfortunately on some devices {@link BluetoothDevice#getName()}
         *            always returns <code>null</code>, i.e. Sony Xperia Z1 (C6903) with Android 4.3.
         *            The name has to be parsed manually form the Advertisement packet.
         */
        void onDeviceSelected(@NonNull final BluetoothDevice device, @Nullable final String name);

        /**
         * Fired when scanner dialog has been cancelled without selecting a device.
         */
       // void onDialogCanceled();
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

//        listview.setAdapter(customAdapter);
        listview.setAdapter(adapter = new DeviceListAdapter());



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
//                CustomData item = (CustomData) parent.getItemAtPosition(position);
                stopScan();
                final ExtendedBluetoothDevice d = (ExtendedBluetoothDevice) adapter.getItem(position);
               // listener.onDeviceSelected(d.device, d.name);

                DeviceManager.getInstance().deviceName = d.name;
                DeviceManager.getInstance().selectedDevice = d.device;

                finish();
                close();


            }
        });
        final BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            bluetoothAdapter = manager.getAdapter();
        }
        startScan();
        addBoundDevices();


    }
    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have been granted the Manifest.permission.ACCESS_FINE_LOCATION permission. Now we may proceed with scanning.
                    startScan();
                } else {
                   // permissionRationale.setVisibility(View.VISIBLE);
                   // Toast.makeText(getActivity(), R.string.no_required_permission, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    private void startScan() {
        // Since Android 6.0 we need to obtain Manifest.permission.ACCESS_FINE_LOCATION to be able to scan for
        // Bluetooth LE devices. This is related to beacons as proximity devices.
        // On API older than Marshmallow the following code does nothing.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // When user pressed Deny and still wants to use this functionality, show the rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ) {
                //permissionRationale.setVisibility(View.VISIBLE);
                return;
            }

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }

        // Hide the rationale message, we don't need it anymore.
        if (permissionRationale != null)
            permissionRationale.setVisibility(View.GONE);

        adapter.clearDevices();
     //   scanButton.setText(R.string.scanner_action_cancel);

        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        final ScanSettings settings = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(1000).setUseHardwareBatchingIfSupported(false).build();
        final List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setServiceUuid(uuid).build());
        scanner.startScan(filters, settings, scanCallback);

        scanning = true;

        handler.postDelayed(() -> {
            if (scanning) {
                stopScan();
            }
        }, SCAN_DURATION);



    }

    private void stopScan() {
        if (scanning) {
        //    scanButton.setText(R.string.scanner_action_scan);

            final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            scanner.stopScan(scanCallback);

            scanning = false;
        }
    }


    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, @NonNull final ScanResult result) {
            // do nothing
        }

        @Override
        public void onBatchScanResults(@NonNull final List<ScanResult> results) {
            adapter.update(results);
        }

        @Override
        public void onScanFailed(final int errorCode) {
            // should never be called
        }
    };

    private void addBoundDevices() {
        final Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        adapter.addBondedDevices(devices);
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
    private class CustomAdapter extends ArrayAdapter<ScanActivity.CustomData> {

        private LayoutInflater mLayoutInflater;

        public CustomAdapter(Context context, int resource, List<ScanActivity.CustomData> objects) {
            super(context, resource, objects);
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 특정 행(position)의 데이터를 구함
            ScanActivity.CustomData item = (ScanActivity.CustomData) getItem(position);

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