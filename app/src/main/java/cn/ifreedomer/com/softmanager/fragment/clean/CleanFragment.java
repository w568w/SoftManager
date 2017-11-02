package cn.ifreedomer.com.softmanager.fragment.clean;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.clean.BigFileCleanActivity;
import cn.ifreedomer.com.softmanager.activity.clean.DeepCleanActivity;
import cn.ifreedomer.com.softmanager.activity.clean.GarbageActivity;
import cn.ifreedomer.com.softmanager.activity.clean.QQCleanActivity;
import cn.ifreedomer.com.softmanager.bean.CleanCardInfo;
import cn.ifreedomer.com.softmanager.util.PackageUtil;
import cn.ifreedomer.com.softmanager.widget.ItemCardView;

/**
 * @author:eavawu
 * @since: 26/10/2017.
 * TODO:
 */

public class CleanFragment extends Fragment implements View.OnClickListener {
    @InjectView(R.id.iv_clean)
    ImageView ivClean;
    @InjectView(R.id.qq_card)
    ItemCardView qqCardView;
    @InjectView(R.id.big_file_card)
    ItemCardView bigFileCardView;
    @InjectView(R.id.garbage_card)
    ItemCardView garbageCardView;
    @InjectView(R.id.deep_card)
    ItemCardView deepCardView;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;


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
        mBtnClean.setOnClickListener(this);
        garbageCardView.setOnClickListener(this);
    }

    private void setData() {
        garbageCardView.setData(new CleanCardInfo(getString(R.string.garbage_clean), R.mipmap.clean));
        deepCardView.setData(new CleanCardInfo(getString(R.string.deep_clean), R.mipmap.deep_clean));
        qqCardView.setData(new CleanCardInfo(getString(R.string.qq_clean), R.mipmap.qq));
        bigFileCardView.setData(new CleanCardInfo(getString(R.string.big_file), R.mipmap.file));


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
            case R.id.deep_card:
                intent = new Intent(getActivity(), DeepCleanActivity.class);
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
//                PackageInfoManager.getInstance().clearCache();
                break;
            default:
                break;
        }

    }
}
