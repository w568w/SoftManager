package cn.ifreedomer.com.softmanager.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.PayResult;
import cn.ifreedomer.com.softmanager.bean.RespResult;
import cn.ifreedomer.com.softmanager.db.DBActionUtils;
import cn.ifreedomer.com.softmanager.listener.LoadAllComponentListener;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.network.requestservice.ServiceManager;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ShellUtils;
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
    @InjectView(R.id.btn_load_component)
    Button btnLoadComponent;
    @InjectView(R.id.activity_test)
    LinearLayout activityTest;
    @InjectView(R.id.btn_load_action)
    Button btnLoadAction;
    @InjectView(R.id.btn_moveToSystem)
    Button btnMoveToSystem;


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
        btnLoadComponent.setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
        btnLoadAction.setOnClickListener(this);
        btnMoveToSystem.setOnClickListener(this);

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
                if (PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                    mountTest();
                }
//                List<PermissionDetail> allPermission = PermissionManager.getInstance().loadAllPermission("com.tencent.qqpimsecure");
//                for (int i = 0; i < allPermission.size(); i++) {
//                    LogUtil.e(TAG, allPermission.get(i).toString());
//                }
                break;
            case R.id.btn_parse_permission_xml:
                PermissionManager.getInstance().loadPermissionConfig();
                break;
            case R.id.btn_recharge:
                ServiceManager.getPayInfo("111111111111111").subscribeOn(Schedulers.io()).subscribe(s -> {
                    if (s.getResultCode() == RespResult.FAILED) {
                        runOnUiThread(() -> Toast.makeText(TestActivity.this, s.getMsg(), Toast.LENGTH_SHORT).show());
                        return;
                    }
                    PayTask payTask = new PayTask(TestActivity.this);
                    Log.i("msp", "order info =>" + s.getData().getPayInfo());
//                    String content = "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22soft_1510987025%22%2C%22passback_params%22%3A%22imei%22%3A%221234567222%7D&method=alipay.trade.app.pay&notify_url=www.ifreedomer.com%2Fpay%2FalipayNotify&app_id=2017082408350806&sign_type=RSA&version=1.0&timestamp=2017-11-18+02%3A37%3A05&sign=kCx4BarZHA0bb0XiIeWfcvFEF%2Bl7w9r1h4rSzpvySgO1bYvq4qeE9zL1M%2FbwrU2lyKIlr08ImI45cXGEfsl62Ji79b3k5Zs%2Bq2nmBYmyoEWojNM7sQnBqAzQLDLpNaJHBvnzJZKqnxeDqZmJXih2gj%2BTYbQ81KQsYWbYqZNpKeA%3D";
//                                        String content = "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22soft_1510671419%22%7D&method=alipay.trade.app.pay&app_id=2017082408350806&sign_type=RSA&version=1.0&timestamp=2016-07-29+16%3A55%3A53&sign=FGoy2Vpk%2F2Tmyyxa8%2FIVf6%2Bp%2BU%2Bu0VDz3rJ%2BsWo6UTexyE735gvqUhbvtLMHZY4%2BkkNN2ApJeWUVxS5mT373jMQ2NvX3bbTLmSAttXoi4JxiHQKrgXKGHmCrFzpBygZcsg3kaEnrimkjDug5usagVSSXq%2B8jVHhGbASkJPiSrow%3D";
                    String content = "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22s15113619941363%22%2C%22passback_params%22%3A%221231111111111111%22%7D&method=alipay.trade.app.pay&notify_url=www.ifreedomer.com%2Fpay%2FalipayNotify&app_id=2017082408350806&sign_type=RSA&version=1.0&timestamp=2017-11-22+10%3A46%3A34&sign=SplVyWeBErO8id9V4ZFzrhd1sjvCQS5AxBAPhRwwzF3lLubylblfbulPGbRDYX0vUUUlmjaFFvHsbAYUwlIITjjXAD%2FX2SdMyb3f04Lb%2By9%2BTJGdajIPnfiR%2FZgUUrV63Zre8JYaw%2BMeBstjdxW1kPBn%2B%2BeoCzVVWl%2F7I343b1k%3D";
                    Map<String, String> result = payTask.payV2(content, true);
                    Log.i("msp", result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                });


                break;

            case R.id.btn_load_component:
                GlobalDataManager.getInstance().getThreadPool().execute(() -> {
                    PackageInfoManager.getInstance().loadAllComponent(new LoadAllComponentListener() {
                        @Override
                        public void loadFinish() {

                        }
                    });
                    LogUtil.d(TAG, "loadAllComponent");
                });
                break;
            case R.id.btn_load_action:
                Map<String, String> stringStringMap = DBActionUtils.loadActionMap(this);
                Log.d(TAG, "stringStringMap = " + stringStringMap.toString());
                break;
            case R.id.btn_moveToSystem:
                boolean b = PermissionManager.getInstance().checkOrRequestedRootPermission();

                break;
            default:
                break;
        }
    }





    private void mountTest() {
        if (PermissionManager.getInstance().checkOrRequestedRootPermission()) {
            GlobalDataManager.getInstance().getThreadPool().execute(() -> {
                AppInfo appInfo = new AppInfo();
                appInfo.setPackname("com.xiaomi.pass");
                appInfo.setCodePath("/system/app/XMPass/XMPass.apk");
                uninstallSystemApp(appInfo);
            });
        }
    }


    public static boolean uninstallSystemApp(AppInfo appItem) {
        ShellUtils.CommandResult mount = ShellUtils.execCommand("mount", true);
        ShellUtils.CommandResult mount1 = ShellUtils.execCommand("mount -o remount,rw /dev/block/system /system", true);


//        String absolutePath = Environment.getExternalStorageDirectory().getName();
//        ShellUtils.CommandResult mount1 = ShellUtils.execCommand("cd " + absolutePath, true);
//
//        String str2 = "/mnt/shell/emulated/" + Environment.getExternalStorageDirectory().getName();
//
//
//        ShellUtils.CommandResult mount2 = ShellUtils.execCommand("cd " + str2, true);
//
//        ShellUtils.CommandResult mount5 = ShellUtils.execCommand("ln -s \"" + str2 + "\" \"" + absolutePath + "\"", true);
//
//        ShellUtils.CommandResult mount6 = null;
//        ShellUtils.CommandResult mount7 = null;
//        ShellUtils.CommandResult mount8 = null;
//        ShellUtils.execCommand("su",false);
//        if (mount5.errorMsg != null && mount5.errorMsg.length() > 0 && mount5.errorMsg.contains("Read-only file system")) {
//            mount6 = ShellUtils.execCommand("mount -o rw,remount /", false);
//            mount7 = ShellUtils.execCommand("ln -s \"" + str2 + "\" \"" + absolutePath + "\"", true);
//            mount8 = ShellUtils.execCommand("mount -o ro,remount /", false);
//
//            return false;
//        }
//
//
//        ShellUtils.CommandResult mount3 = ShellUtils.execCommand("mount -o rw,remount /", true);
//
//
//        String command = "rm " + appItem.getCodePath() + "\n";
//        command += "pm uninstall " + appItem.getPackname() + "\n";
//        ShellUtils.CommandResult mount4 = ShellUtils.execCommand(command, true);
//
//
        LogUtil.d(TAG, "MOUNT = " + mount.toString());
        LogUtil.d(TAG, "MOUNT1 = " + mount1.toString());
//        LogUtil.d(TAG, "mount2 = " + mount2.toString());
//        LogUtil.d(TAG, "mount3 = " + mount3.toString());
//        LogUtil.d(TAG, "mount4 = " + mount4.toString());
//        LogUtil.d(TAG, "mount5 = " + mount5.toString());
//        LogUtil.d(TAG, "mount6 = " + mount6.toString());
//        LogUtil.d(TAG, "mount7 = " + mount7.toString());
//        LogUtil.d(TAG, "mount8 = " + mount8.toString());


        return false;
    }


    private Runnable scanSdcardRunnable = new Runnable() {
        @Override
        public void run() {
            long beginTime = System.currentTimeMillis();
//            FileUtil.scanSDCard4BigFile(Environment.getExternalStorageDirectory().toString(), new FileScanService.ScanListener() {
//                @Override
//                public void onScanStart() {
//                    Log.e(TAG, "onScanStart: ");
//                }
//
//                @Override
//                public void onScanProcess(float process, FileInfo fileInfo) {
//
//                }
//
//
//                @Override
//                public void onScanFinish(float garbageSize, List<FileInfo> garbageList) {
//                    Log.e(TAG, "onScanFinish: garbageSize=" + garbageSize + "MB    garbageList" + garbageList + toString());
//                }
//            });

//            Log.e(TAG, "size: " + bigFiles.size() + "   time:" + (System.currentTimeMillis() - beginTime));
        }
    };
}
