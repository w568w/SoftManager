package cn.ifreedomer.com.softmanager.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.PayResult;
import cn.ifreedomer.com.softmanager.bean.RespResult;
import cn.ifreedomer.com.softmanager.bean.json.PayInfo;
import cn.ifreedomer.com.softmanager.network.requestservice.ServiceManager;
import cn.ifreedomer.com.softmanager.util.HardwareUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author:eavawu
 * @since: 22/11/2017.
 * TODO:支付dialog
 */

public class PayDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = PayDialog.class.getSimpleName();
    @InjectView(R.id.iv_icon)
    ImageView ivIcon;
    @InjectView(R.id.rel_title)
    RelativeLayout relTitle;
    @InjectView(R.id.tv_des)
    TextView tvDes;
    @InjectView(R.id.btn_pay)
    Button btnPay;
    @InjectView(R.id.btn_next)
    Button btnNext;
    private static final int SDK_PAY_FLAG = 1;
    @InjectView(R.id.price_tv)
    TextView priceTv;
    private PayInfo mPayInfo;

    public PayDialog(@NonNull Context context) {
        super(context);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
        ButterKnife.inject(this);
        btnPay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        priceTv.setText(mPayInfo.getPrice() + "");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                dismiss();

                pay(mPayInfo.getPayInfo());


                break;
            case R.id.btn_next:
                dismiss();
                break;

        }
    }


    public void showPay() {
        ServiceManager.getPayInfo(HardwareUtil.getImei()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(respResult -> {
            if (respResult.getResultCode() != RespResult.SUCCESS) {
                Toast.makeText(getContext(), respResult.getMsg(), Toast.LENGTH_SHORT).show();
                return;
            }
            mPayInfo = respResult.getData();
            show();
        }, throwable -> {
            throwable.printStackTrace();
            LogUtil.e(TAG, "getPayInfo =>" + throwable);
        });

    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
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
                        Toast.makeText(PayDialog.this.getOwnerActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayDialog.this.getOwnerActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        }
    };

    private void pay(String orderStr) {
        PayTask payTask = new PayTask(this.getOwnerActivity());
        Log.i("msp", "order info =>" + orderStr);
//                    String content = "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22soft_1510987025%22%2C%22passback_params%22%3A%22imei%22%3A%221234567222%7D&method=alipay.trade.app.pay&notify_url=www.ifreedomer.com%2Fpay%2FalipayNotify&app_id=2017082408350806&sign_type=RSA&version=1.0&timestamp=2017-11-18+02%3A37%3A05&sign=kCx4BarZHA0bb0XiIeWfcvFEF%2Bl7w9r1h4rSzpvySgO1bYvq4qeE9zL1M%2FbwrU2lyKIlr08ImI45cXGEfsl62Ji79b3k5Zs%2Bq2nmBYmyoEWojNM7sQnBqAzQLDLpNaJHBvnzJZKqnxeDqZmJXih2gj%2BTYbQ81KQsYWbYqZNpKeA%3D";
        //                    String content = "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22soft_1510671419%22%7D&method=alipay.trade.app.pay&app_id=2017082408350806&sign_type=RSA&version=1.0&timestamp=2016-07-29+16%3A55%3A53&sign=FGoy2Vpk%2F2Tmyyxa8%2FIVf6%2Bp%2BU%2Bu0VDz3rJ%2BsWo6UTexyE735gvqUhbvtLMHZY4%2BkkNN2ApJeWUVxS5mT373jMQ2NvX3bbTLmSAttXoi4JxiHQKrgXKGHmCrFzpBygZcsg3kaEnrimkjDug5usagVSSXq%2B8jVHhGbASkJPiSrow%3D";
        Map<String, String> result = payTask.payV2(orderStr, true);
        Log.i("msp", result.toString());

        Message msg = new Message();
        msg.what = SDK_PAY_FLAG;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }
}