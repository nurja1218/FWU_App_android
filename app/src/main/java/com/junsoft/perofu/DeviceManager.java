package com.junsoft.perofu;

import android.bluetooth.BluetoothDevice;

public class DeviceManager {
    private static DeviceManager _Instance;

    public String deviceName;
    public BluetoothDevice selectedDevice;

    public String firmwareName;

    //static 객체변수 getter선언 (항상 같은 객체를 가져오게된다.)
    public static DeviceManager getInstance(){
        if(_Instance==null){
            _Instance = new DeviceManager();
        }
        return _Instance;
    }



}
