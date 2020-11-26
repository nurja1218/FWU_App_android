package com.junsoft.perofu;

import android.bluetooth.BluetoothDevice;
import android.net.Uri;

public class DeviceManager {
    private static DeviceManager _Instance;

    public String deviceName;
    public BluetoothDevice selectedDevice;

    public String firmwareName;
    public Uri fwUri;


    //static 객체변수 getter선언 (항상 같은 객체를 가져오게된다.)
    public static DeviceManager getInstance(){
        if(_Instance==null){
            _Instance = new DeviceManager();
        }
        return _Instance;
    }



}
