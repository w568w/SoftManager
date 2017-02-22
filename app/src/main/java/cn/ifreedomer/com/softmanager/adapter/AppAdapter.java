package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.view.View;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.listener.OnUnInstallListener;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @date: 19/02/2017.
 * @todo:
 */
public class AppAdapter extends CommonAdapter<AppInfo> {
    private OnUnInstallListener unInstallListener;

    public AppAdapter(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
    }

    public OnUnInstallListener getUnInstallListener() {
        return unInstallListener;
    }

    public void setUnInstallListener(OnUnInstallListener unInstallListener) {
        this.unInstallListener = unInstallListener;
    }

    public List<AppInfo> getData() {
        return mDatas;
    }


    @Override
    public void convert(ViewHolder holder, final AppInfo appInfo) {
        holder.setText(R.id.tv_appname, appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());
        holder.setText(R.id.tv_appcache, appInfo.getPkgSize() + "");
        holder.setOnClickListener(R.id.btn_uninstall, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unInstallListener != null) {
                    unInstallListener.onUninstall(appInfo);
                }
            }
        });
        //  holder.setText(R.id.info_text,"helloworld");
    }
}
