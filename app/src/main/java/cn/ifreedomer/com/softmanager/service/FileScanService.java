package cn.ifreedomer.com.softmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.util.FileUtil;


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

    public void startScan() {
        asyncTask.execute();
    }

    private AsyncTask<String, Integer, List<FileInfo>> asyncTask = new AsyncTask<String, Integer, List<FileInfo>>() {
        @Override
        protected List<FileInfo> doInBackground(String... params) {

            if (scanListener != null) {
                scanListener.onScanStart();
            }
            return FileUtil.scanSDCard4BigFile("", new ScanListener() {
                @Override
                public void onScanStart() {

                }

                @Override
                public void onScanProcess(float process) {
                    publishProgress((int) process);
                }

                @Override
                public void onScanFinish(float garbageSize, List<FileInfo> garbageList) {
                    publishProgress(100);
                }
            });

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(List<FileInfo> fileInfos) {

        }
    };

    public void setScanListener(ScanListener scanListener) {
        this.scanListener = scanListener;
    }


    public FileScanService() {

    }


}
