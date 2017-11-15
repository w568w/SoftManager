package cn.ifreedomer.com.softmanager.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.bean.PayResult;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.network.requestservice.ServiceManager;
import cn.ifreedomer.com.softmanager.service.FileScanService;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import io.reactivex.schedulers.Schedulers;

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
    @InjectView(R.id.btn_recharge)
    Button btnRecharge;

    private static final int SDK_PAY_FLAG = 1;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(TestActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(TestActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);
        btnSdcard.setOnClickListener(this);
        btnDeleteCache.setOnClickListener(this);
        btnGetPermission.setOnClickListener(this);
        btnParsePermissionXml.setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
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
            case R.id.btn_recharge:
                ServiceManager.getPayInfo("1234").subscribeOn(Schedulers.io()).subscribe(s -> {
                    PayTask payTask = new PayTask(TestActivity.this);
                    Log.i(TAG, "order info =>" + s);
                    //                    String content = "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22soft_1510671419%22%7D&method=alipay.trade.app.pay&app_id=2017082408350806&sign_type=RSA&version=1.0&timestamp=2016-07-29+16%3A55%3A53&sign=FGoy2Vpk%2F2Tmyyxa8%2FIVf6%2Bp%2BU%2Bu0VDz3rJ%2BsWo6UTexyE735gvqUhbvtLMHZY4%2BkkNN2ApJeWUVxS5mT373jMQ2NvX3bbTLmSAttXoi4JxiHQKrgXKGHmCrFzpBygZcsg3kaEnrimkjDug5usagVSSXq%2B8jVHhGbASkJPiSrow%3D";
                    Map<String, String> result = payTask.payV2(s, true);
                    Log.i("msp", result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                });


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
