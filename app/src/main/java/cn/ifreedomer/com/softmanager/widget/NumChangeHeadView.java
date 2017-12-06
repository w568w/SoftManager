package cn.ifreedomer.com.softmanager.widget;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ifreedomer.com.softmanager.R;


/**
 * @author:eavawu
 * @since: 31/10/2017.
 * TODO:
 */

public class NumChangeHeadView extends RelativeLayout {

    private TextView mTvScanning;
    private TextView mTvScanTotal;

    public NumChangeHeadView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.garbage_headerview, this);
        mTvScanning = (TextView) findViewById(R.id.tv_scanning);
        mTvScanTotal = (TextView) findViewById(R.id.tv_scan_total);
    }


    public void setScanningText(String scanningPath) {
        mTvScanning.setText(scanningPath);
    }

    public void setScanTotal(String scanTotal) {
        mTvScanTotal.setText(scanTotal);
    }
}
