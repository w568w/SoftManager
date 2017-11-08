package cn.ifreedomer.com.softmanager.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.service.FileScanService;
import cn.ifreedomer.com.softmanager.util.FileUtil;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = TestActivity.class.getSimpleName();
    @InjectView(R.id.btn_sdcard)
    Button btnSdcard;

    @InjectView(R.id.btn_delete_cache)
    Button btnDeleteCache;
    @InjectView(R.id.btn_get_permission)
    Button btnGetPermission;
    @InjectView(R.id.btn_parse_permission_xml)
    Button btnParsePermissionXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);
        btnSdcard.setOnClickListener(this);
        btnDeleteCache.setOnClickListener(this);
        btnGetPermission.setOnClickListener(this);
        btnParsePermissionXml.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sdcard:
                GlobalDataManager.getInstance().getThreadPool().execute(scanSdcardRunnable);
                break;
            case R.id.btn_delete_cache:
//                PackageInfoManager.getInstance().clearCache();
                break;
            case R.id.btn_get_permission:
//                List<PermissionDetail> allPermission = PermissionManager.getInstance().loadAllPermission("com.tencent.qqpimsecure");
//                for (int i = 0; i < allPermission.size(); i++) {
//                    LogUtil.e(TAG, allPermission.get(i).toString());
//                }
                break;
            case R.id.btn_parse_permission_xml:
                PermissionManager.getInstance().loadPermissionConfig();
                break;
            default:
                break;
        }
    }


    private Runnable scanSdcardRunnable = new Runnable() {
        @Override
        public void run() {
            long beginTime = System.currentTimeMillis();
            FileUtil.scanSDCard4BigFile(Environment.getExternalStorageDirectory().toString(), new FileScanService.ScanListener() {
                @Override
                public void onScanStart() {
                    Log.e(TAG, "onScanStart: ");
                }

                @Override
                public void onScanProcess(float process) {
                    Log.e(TAG, "onScanProcess: " + process);
                }

                @Override
                public void onScanFinish(float garbageSize, List<FileInfo> garbageList) {
                    Log.e(TAG, "onScanFinish: garbageSize=" + garbageSize + "MB    garbageList" + garbageList + toString());
                }
            });

//            Log.e(TAG, "size: " + bigFiles.size() + "   time:" + (System.currentTimeMillis() - beginTime));
        }
    };
}
