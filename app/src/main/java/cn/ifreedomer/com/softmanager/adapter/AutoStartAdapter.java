package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.util.AutoStartInfo;
import cn.ifreedomer.com.softmanager.util.Terminal;


/**
 * @author wuyihua
 * @Date 2017/2/23
 */

public class AutoStartAdapter extends com.zhy.base.adapter.recyclerview.CommonAdapter<AutoStartInfo> {

    public AutoStartAdapter(Context context, int layoutId, List<AutoStartInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final AutoStartInfo autoStartInfo) {
        holder.setText(R.id.tv_appname, autoStartInfo.getLabel());
        holder.setImageDrawable(R.id.iv_icon, autoStartInfo.getIcon());
        holder.setText(R.id.tv_appcache, autoStartInfo.getDesc() + "");
        Switch switchWidget = holder.getView(R.id.swith_auto);
        switchWidget.setChecked(autoStartInfo.isEnable());

        switchWidget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Terminal.hasRootPermission()) {
                    Terminal.enableApp(autoStartInfo.getPackageName());
                    autoStartInfo.setEnable(true);
                } else {
                    Terminal.disableApp(autoStartInfo.getPackageName());
                    autoStartInfo.setEnable(false);
                }
            }
        });
    }
}
