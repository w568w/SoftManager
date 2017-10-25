package cn.ifreedomer.com.softmanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.util.SystemUtil;


/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class HardwareHeadView extends RelativeLayout {
    public HardwareHeadView(Context context) {
        super(context);
        initView(context);
    }


    public HardwareHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.hareware_header, this);
        TextView modelTv = (TextView) findViewById(R.id.tv_system_model);
        TextView versionTv = (TextView) findViewById(R.id.tv_version);

        String deviceBrand = SystemUtil.getSystemModel();
        modelTv.setText(deviceBrand);

        String systemVersion = SystemUtil.getSystemVersion();
        String systemShowContent = getContext().getString(R.string.system_version) + systemVersion;
        versionTv.setText(systemShowContent);
    }


}
