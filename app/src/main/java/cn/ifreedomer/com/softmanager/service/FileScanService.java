package cn.ifreedomer.com.softmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import cn.ifreedomer.com.softmanager.bean.FileInfo;


public class FileScanService extends Service {
    protected ScanListener scanListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new FileScanBinder();
    }

    public interface ScanListener {
        void onScanStart();

        void onScanProcess(float process);

        void onScanFinish(float garbageSize, List<FileInfo> garbageList);
    }

    public class FileScanBinder extends Binder {
        public FileScanService getService() {
            return FileScanService.this;
        }
    }



    public void setScanListener(ScanListener scanListener) {
        this.scanListener = scanListener;
    }


    public FileScanService() {

    }


}
