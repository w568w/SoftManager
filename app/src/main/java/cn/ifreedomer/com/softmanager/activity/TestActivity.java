package cn.ifreedomer.com.softmanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.util.FileUtil;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = TestActivity.class.getSimpleName();
    @InjectView(R.id.btn_sdcard)
    Button btnSdcard;
    @InjectView(R.id.activity_test)
    RelativeLayout activityTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);
        btnSdcard.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sdcard:
                GlobalDataManager.getInstance().getThreadPool().execute(scanSdcardRunnable);
                break;
            default:
                break;
        }
    }


    private Runnable scanSdcardRunnable = new Runnable() {
        @Override
        public void run() {
            long beginTime = System.currentTimeMillis();
            List<FileInfo> bigFiles = FileUtil.getBigFiles();

            Log.e(TAG, "size: " + bigFiles.size() + "   time:" + (System.currentTimeMillis() - beginTime));
        }
    };
}
