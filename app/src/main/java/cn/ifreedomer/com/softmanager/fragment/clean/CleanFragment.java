package cn.ifreedomer.com.softmanager.fragment.clean;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.clean.BigFileCleanActivity;
import cn.ifreedomer.com.softmanager.activity.clean.GarbageActivity;
import cn.ifreedomer.com.softmanager.activity.clean.MemoryCleanActivity;
import cn.ifreedomer.com.softmanager.activity.clean.QQCleanActivity;
import cn.ifreedomer.com.softmanager.bean.CleanCardInfo;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.MemoryUtils;
import cn.ifreedomer.com.softmanager.util.PackageUtil;
import cn.ifreedomer.com.softmanager.widget.CustomProgressView;
import cn.ifreedomer.com.softmanager.widget.ItemCardView;

/**
 * @author:eavawu
 * @since: 26/10/2017.
 * TODO:
 */

public class CleanFragment extends Fragment implements View.OnClickListener {
    //    @InjectView(R.id.iv_clean)
//    ImageView ivClean;
    @InjectView(R.id.qq_card)
    ItemCardView qqCardView;
    @InjectView(R.id.big_file_card)
    ItemCardView bigFileCardView;
    @InjectView(R.id.garbage_card)
    ItemCardView garbageCardView;
    @InjectView(R.id.memory_clean)
    ItemCardView deepCardView;
    @InjectView(R.id.big)
    CustomProgressView big;
    @InjectView(R.id.big_percent_textview)
    TextView bigPercentTextview;
    @InjectView(R.id.big_percent_layout)
    LinearLayout bigPercentLayout;
    @InjectView(R.id.big_available_memory_textview)
    TextView bigAvailableMemoryTextview;
    @InjectView(R.id.small)
    CustomProgressView small;
    @InjectView(R.id.small_percent_textview)
    TextView smallPercentTextview;
    @InjectView(R.id.small_percent_layout)
    LinearLayout smallPercentLayout;
    @InjectView(R.id.percent_view_layout)
    RelativeLayout percentViewLayout;
    private static final int MSG_UPDATE_PERCENT = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_PERCENT:
                    small.setProgress((int) mRamAvPercent);
                    big.setProgress((int) mSdAvPercent);
                    bigAvailableMemoryTextview.setText("剩余" + DataTypeUtil.getTextBySize(mSdAvalible));
                    break;

            }
        }
    };
    private long mRamAvPercent;
    private long mSdAvPercent;
    private long mSdAvalible;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clean, container, false);
        ButterKnife.inject(this, view);
        initView();
        setData();
        return view;
    }

    private void initView() {

        bigFileCardView.setOnClickListener(this);
        deepCardView.setOnClickListener(this);
        qqCardView.setOnClickListener(this);
//        mBtnClean.setOnClickListener(this);
        garbageCardView.setOnClickListener(this);
        big.setBig(true);
        big.setPercentView(bigPercentTextview);
        small.setPercentView(smallPercentTextview);
        small.setBig(false);

    }

    private void setData() {
        garbageCardView.setData(new CleanCardInfo(getString(R.string.garbage_clean), R.mipmap.clean));
        deepCardView.setData(new CleanCardInfo(getString(R.string.memory_clean), R.mipmap.speed));
        qqCardView.setData(new CleanCardInfo(getString(R.string.qq_clean), R.mipmap.qq));
        bigFileCardView.setData(new CleanCardInfo(getString(R.string.big_file), R.mipmap.file));
        updataPercentView();
    }

    private void updataPercentView() {
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            long sdMax = MemoryUtils.getTotalExternalMemorySize();
            mSdAvalible = MemoryUtils.getAvailableExternalMemorySize();
            long romMax = MemoryUtils.getTotalInternalMemorySize();
            long romAva = MemoryUtils.getAvailableInternalMemorySize();
            mRamAvPercent = romAva * 100 / romMax;
            mSdAvPercent = (sdMax - mSdAvalible) * 100 / sdMax;
            mHandler.sendEmptyMessage(MSG_UPDATE_PERCENT);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.big_file_card:
                intent = new Intent(getActivity(), BigFileCleanActivity.class);
                startActivity(intent);
                break;
            case R.id.memory_clean:
                intent = new Intent(getActivity(), MemoryCleanActivity.class);
                startActivity(intent);
                break;
            case R.id.garbage_card:
                intent = new Intent(getActivity(), GarbageActivity.class);
                startActivity(intent);
                break;
            case R.id.qq_card:
                if (!PackageUtil.isAppInstalled(getActivity(), "com.tencent.mobileqq")) {
                    Toast.makeText(getActivity(), R.string.not_install_qq, Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getActivity(), QQCleanActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_clean:
                intent = new Intent(getActivity(), GarbageActivity.class);
                startActivity(intent);
//                PackageInfoManager.getInstance().clearCache();
                break;
            default:
                break;
        }

    }
}
